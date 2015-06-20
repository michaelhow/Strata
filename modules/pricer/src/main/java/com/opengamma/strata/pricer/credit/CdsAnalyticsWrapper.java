package com.opengamma.strata.pricer.credit;

import com.google.common.collect.Lists;
import com.opengamma.analytics.financial.credit.isdastandardmodel.*;
import com.opengamma.strata.basics.currency.CurrencyAmount;
import com.opengamma.strata.basics.currency.MultiCurrencyAmount;
import com.opengamma.strata.basics.date.BusinessDayConvention;
import com.opengamma.strata.basics.date.DayCount;
import com.opengamma.strata.basics.date.DayCounts;
import com.opengamma.strata.basics.date.HolidayCalendar;
import com.opengamma.strata.basics.schedule.StubConvention;
import com.opengamma.strata.finance.credit.ExpandedCds;
import com.opengamma.strata.finance.credit.type.CdsConvention;
import com.opengamma.strata.finance.credit.type.IsdaYieldCurveConvention;
import com.opengamma.strata.market.curve.IsdaCreditCurveParRates;
import com.opengamma.strata.market.curve.IsdaYieldCurveParRates;
import com.opengamma.strata.market.curve.IsdaYieldCurveUnderlyingType;
import com.opengamma.strata.pricer.PricingException;

import java.time.LocalDate;
import java.time.Period;

/**
 * Single point for interacting with the underlying Analytics layer for CDS pricing
 * <p>
 * Translation from Strata business objects such as DayCount and StubMethod is done here
 * The translation of underlying types for the yield curve is performed here
 * Par rate representations of the curves are calibrated and converted to ISDA calibrated curves
 * Present value of the expanded CDS product (single name or index) is calculated here
 */
public class CdsAnalyticsWrapper {

  /**
   * DayCount used with calculating time during curve calibration
   * The model expects ACT_365F, but this value is not on the trade or the convention
   */
  private final static DayCount s_curveDayCount = DayCounts.ACT_365F;

  /**
   * If protectStart = true, then protections starts at the beginning of the day, otherwise it is at the end.
   * The model expects this but it is not a property of the trade or convention
   * protectionFromStartOfDay If true the protection is from the start of day and the effective accrual start
   * and end dates are one day less. The exception is the final accrual end date which should have one day added
   * (if  protectionFromStartOfDay = true) in the final CDSCouponDes to compensate for this, so the
   * accrual end date is just the CDS maturity. The effect of having protectionFromStartOfDay = true
   * is to add an extra day of protection.
   */
  private final static boolean s_protectStart = true;

  /**
   * ISDA Standard model implementation in analytics
   */
  private final static AnalyticCDSPricer s_calculator = new AnalyticCDSPricer();

  /**
   * Calculate present value on the specified valuation date
   *
   * @param valuationDate date that present value is calculated on, also date that curves will be calibrated to
   * @param product       expanded cds product
   * @param yieldCurve    par rates representation of the ISDA yield curve
   * @param creditCurve   par rates representaion of the ISDA credit curve
   * @param recoveryRate  recovery rate for the reference entity/issue
   * @return Present value of the expanded CDS product
   */
  public static MultiCurrencyAmount price(
      LocalDate valuationDate,
      ExpandedCds product,
      IsdaYieldCurveParRates yieldCurve,
      IsdaCreditCurveParRates creditCurve,
      double recoveryRate) {

    CDSAnalytic cdsAnalytic = toAnalytic(valuationDate, product, recoveryRate);

    ISDACompliantYieldCurve yieldCurveAnalytics = toIsdaDiscountCurve(
        valuationDate,
        yieldCurve
    );

    ISDACompliantCreditCurve creditCurveAnalytics = toIsdaCreditCurve(
        valuationDate,
        creditCurve,
        yieldCurveAnalytics,
        recoveryRate
    );

    double coupon = product.getCoupon();

    double pv = s_calculator.pv(
        cdsAnalytic,
        yieldCurveAnalytics,
        creditCurveAnalytics,
        coupon,
        PriceType.DIRTY,
        0D
    );

    int sign = product.getBuySellProtection().isBuy() ? 1 : -1;
    double notional = product.getNotional();
    double adjusted = pv * notional * sign;
    double upfrontFeeAmount = priceUpfrontFee(
        valuationDate, product.upfrontFeeAmount, product.getUpfrontFeePaymentDate(), yieldCurveAnalytics) * sign;
    double adjustedPlusFee = adjusted + upfrontFeeAmount;

    CurrencyAmount currencyAmount = CurrencyAmount.of(product.getCurrency(), adjustedPlusFee);
    return MultiCurrencyAmount.of(currencyAmount);

  }

  /**
   * The fee is always calculated as being payable by the protection buyer.
   * If the seller should pay the fee, then a negative amount is used.
   */
  private static double priceUpfrontFee(
      LocalDate valuationDate,
      double amount,
      LocalDate paymentDate,
      ISDACompliantYieldCurve yieldCurve) {

    if (amount == 0D) {
      return 0D; // no fee
    }
    if (!paymentDate.isAfter(valuationDate)) {
      return 0D; // fee already paid
    }
    double feeSettleYearFraction = s_curveDayCount.yearFraction(valuationDate, paymentDate);
    double discountFactor = yieldCurve.getDiscountFactor(feeSettleYearFraction);
    return discountFactor * amount;
  }

  /**
   * convert the interest rate curve par rates to the corresponding analytics form (calibration is performed here)
   */
  private static ISDACompliantYieldCurve toIsdaDiscountCurve(
      LocalDate valuationDate,
      IsdaYieldCurveParRates yieldCurve) {

    try {
      // model does not use floating leg of underlying IRS
      IsdaYieldCurveConvention curveConvention = yieldCurve.getCurveConvention();
      Period swapInterval = curveConvention.getFixedPaymentFrequency().getPeriod();
      DayCount mmDayCount = curveConvention.getMmDayCount();
      DayCount swapDayCount = curveConvention.getFixedDayCount();

      BusinessDayConvention convention = curveConvention.getBadDayConvention();
      HolidayCalendar holidayCalendar = curveConvention.getHolidayCalendar();

      LocalDate spotDate = curveConvention.getSpotDateAsOf(valuationDate);

      ISDAInstrumentTypes[] types = Lists
          .newArrayList(yieldCurve.getYieldCurveInstruments())
          .stream()
          .map(CdsAnalyticsWrapper::mapInstrumentType)
          .toArray(ISDAInstrumentTypes[]::new);
      return new ISDACompliantYieldCurveBuild(
          valuationDate,
          spotDate,
          types,
          yieldCurve.getYieldCurvePoints(),
          translateDayCount(mmDayCount),
          translateDayCount(swapDayCount),
          swapInterval,
          translateDayCount(s_curveDayCount),
          convention,
          holidayCalendar
      ).build(yieldCurve.getParRates());
    } catch (Exception e) {
      throw new PricingException("Error converting the Isda Discount Curve: " + e.getMessage(), e);
    }
  }

  /**
   * convert the credit curve par rates to the corresponding analytics form (calibration is performed here)
   */
  private static ISDACompliantCreditCurve toIsdaCreditCurve(
      LocalDate valuationDate,
      IsdaCreditCurveParRates curveCurve,
      ISDACompliantYieldCurve yieldCurve,
      double recoveryRate
  ) {
    try {
      CdsConvention cdsConvention = curveCurve.getCdsConvention();
      return new FastCreditCurveBuilder(
          AccrualOnDefaultFormulae.OrignalISDA,
          ISDACompliantCreditCurveBuilder.ArbitrageHandling.Fail
      ).calibrateCreditCurve(
          valuationDate,
          cdsConvention.calcUnadjustedStepInDate(valuationDate),
          cdsConvention.calcAdjustedSettleDate(valuationDate),
          cdsConvention.calcAdjustedStartDate(valuationDate),
          curveCurve.getCreditCurveEndDatePoints(valuationDate),
          curveCurve.getParRates(),
          cdsConvention.getPayAccOnDefault(),
          cdsConvention.getPaymentFrequency().getPeriod(),
          translateStubType(cdsConvention.getStubConvention()),
          s_protectStart,
          yieldCurve,
          recoveryRate
      );
    } catch (Exception e) {
      throw new PricingException("Error converting the Isda Credit Curve: " + e.getMessage(), e);
    }
  }

  /**
   * convert the expanded cds product to the corresponding analytics form
   */
  private static CDSAnalytic toAnalytic(LocalDate valuationDate, ExpandedCds product, double recoveryRate) {
    try {
      return new CDSAnalytic(
          valuationDate,
          valuationDate.plusDays(1),
          valuationDate,
          product.getAccStartDate(),
          product.getEndDate(),
          product.isPayAccOnDefault(),
          product.getPaymentInterval(),
          translateStubType(product.getStubConvention()),
          s_protectStart,
          recoveryRate,
          product.getBusinessdayAdjustmentConvention(),
          product.getCalendar(),
          translateDayCount(product.getAccrualDayCount()),
          translateDayCount(s_curveDayCount)
      );
    } catch (Exception e) {
      throw new PricingException("Error converting the trade to an analytic: " + e.getMessage(), e);
    }
  }

  //------------------------------------------------------------------------------------------

  /**
   * convert type of interest curve underlying to the corresponding analytics value
   */
  private static ISDAInstrumentTypes mapInstrumentType(IsdaYieldCurveUnderlyingType input) {
    switch (input) {
      case IsdaMoneyMarket:
        return ISDAInstrumentTypes.MoneyMarket;
      case IsdaSwap:
        return ISDAInstrumentTypes.Swap;
      default:
        throw new IllegalStateException("Unexpected underlying type, only MoneyMarket and Swap supported: " + input);
    }
  }

  /**
   * convert day count to corresponding analytics value
   */
  private static com.opengamma.analytics.convention.daycount.DayCount translateDayCount(DayCount from) {
    switch (from.getName()) {
      case "Act/365F":
        return com.opengamma.analytics.convention.daycount.DayCounts.ACT_365F;
      case "30E/360":
        return com.opengamma.analytics.convention.daycount.DayCounts.THIRTY_E_360;
      case "30/360 ISDA":
        return com.opengamma.analytics.convention.daycount.DayCounts.THIRTY_E_360;
      case "Act/360":
        return com.opengamma.analytics.convention.daycount.DayCounts.ACT_360;
      default:
        throw new IllegalStateException("Unknown daycount " + from);
    }
  }

  /**
   * convert stub type to corresponding analytics value
   */
  private static com.opengamma.analytics.financial.credit.isdastandardmodel.StubType translateStubType(StubConvention from) {
    switch (from) {
      case SHORT_INITIAL:
        return com.opengamma.analytics.financial.credit.isdastandardmodel.StubType.FRONTSHORT;
      case SHORT_FINAL:
        return com.opengamma.analytics.financial.credit.isdastandardmodel.StubType.BACKSHORT;
      default:
        throw new IllegalStateException("Unknown stub convention: " + from);
    }
  }


}