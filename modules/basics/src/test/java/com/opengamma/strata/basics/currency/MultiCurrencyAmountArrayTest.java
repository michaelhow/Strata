/**
 * Copyright (C) 2016 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.strata.basics.currency;

import static com.opengamma.strata.basics.currency.Currency.CAD;
import static com.opengamma.strata.basics.currency.Currency.EUR;
import static com.opengamma.strata.basics.currency.Currency.GBP;
import static com.opengamma.strata.basics.currency.Currency.USD;
import static com.opengamma.strata.collect.TestHelper.assertThrows;
import static com.opengamma.strata.collect.TestHelper.assertThrowsIllegalArg;
import static com.opengamma.strata.collect.TestHelper.coverBeanEquals;
import static com.opengamma.strata.collect.TestHelper.coverImmutableBean;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.data.Offset.offset;

import java.util.List;
import java.util.Map;

import org.testng.annotations.Test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.opengamma.strata.collect.array.DoubleArray;

/**
 * Test {@link MultiCurrencyAmountArray}.
 */
@Test
public class MultiCurrencyAmountArrayTest {

  private static final MultiCurrencyAmountArray VALUES_ARRAY =
      MultiCurrencyAmountArray.of(
          ImmutableList.of(
              MultiCurrencyAmount.of(
                  CurrencyAmount.of(Currency.GBP, 20),
                  CurrencyAmount.of(Currency.USD, 30),
                  CurrencyAmount.of(Currency.EUR, 40)),
              MultiCurrencyAmount.of(
                  CurrencyAmount.of(Currency.GBP, 21),
                  CurrencyAmount.of(Currency.USD, 32),
                  CurrencyAmount.of(Currency.EUR, 43)),
              MultiCurrencyAmount.of(
                  CurrencyAmount.of(Currency.GBP, 22),
                  CurrencyAmount.of(Currency.USD, 33),
                  CurrencyAmount.of(Currency.EUR, 44))));

  //-------------------------------------------------------------------------
  public void test_of() {
    assertThat(VALUES_ARRAY.getValues(Currency.GBP)).isEqualTo(DoubleArray.of(20, 21, 22));
    assertThat(VALUES_ARRAY.getValues(Currency.USD)).isEqualTo(DoubleArray.of(30, 32, 33));
    assertThat(VALUES_ARRAY.getValues(Currency.EUR)).isEqualTo(DoubleArray.of(40, 43, 44));

    MultiCurrencyAmountArray raggedArray = MultiCurrencyAmountArray.of(
        ImmutableList.of(
            MultiCurrencyAmount.of(
                CurrencyAmount.of(Currency.EUR, 4)),
            MultiCurrencyAmount.of(
                CurrencyAmount.of(Currency.GBP, 21),
                CurrencyAmount.of(Currency.USD, 32),
                CurrencyAmount.of(Currency.EUR, 43)),
            MultiCurrencyAmount.of(
                CurrencyAmount.of(Currency.EUR, 44))));

    assertThat(raggedArray.size()).isEqualTo(3);
    assertThat(VALUES_ARRAY.getCurrencies()).isEqualTo(ImmutableSet.of(Currency.GBP, Currency.USD, Currency.EUR));
    assertThat(raggedArray.getValues(Currency.GBP)).isEqualTo(DoubleArray.of(0, 21, 0));
    assertThat(raggedArray.getValues(Currency.USD)).isEqualTo(DoubleArray.of(0, 32, 0));
    assertThat(raggedArray.getValues(Currency.EUR)).isEqualTo(DoubleArray.of(4, 43, 44));
    assertThrowsIllegalArg(() -> raggedArray.getValues(Currency.AUD));
  }

  public void test_of_function() {
    MultiCurrencyAmount mca1 = MultiCurrencyAmount.of(CurrencyAmount.of(Currency.GBP, 10), CurrencyAmount.of(Currency.USD, 20));
    MultiCurrencyAmount mca2 = MultiCurrencyAmount.of(CurrencyAmount.of(Currency.GBP, 10), CurrencyAmount.of(Currency.EUR, 30));
    MultiCurrencyAmount mca3 = MultiCurrencyAmount.of(CurrencyAmount.of(Currency.USD, 40));
    List<MultiCurrencyAmount> amounts = ImmutableList.of(mca1, mca2, mca3);

    MultiCurrencyAmountArray test = MultiCurrencyAmountArray.of(3, i -> amounts.get(i));
    assertThat(test.get(0)).isEqualTo(mca1.plus(Currency.EUR, 0));
    assertThat(test.get(1)).isEqualTo(mca2.plus(Currency.USD, 0));
    assertThat(test.get(2)).isEqualTo(mca3.plus(Currency.GBP, 0).plus(Currency.EUR, 0));
  }

  public void test_of_map() {
    MultiCurrencyAmountArray array = MultiCurrencyAmountArray.of(
        ImmutableMap.of(
            Currency.GBP, DoubleArray.of(20, 21, 22),
            Currency.USD, DoubleArray.of(30, 32, 33),
            Currency.EUR, DoubleArray.of(40, 43, 44)));

    assertThat(array).isEqualTo(VALUES_ARRAY);

    assertThrowsIllegalArg(
        () -> MultiCurrencyAmountArray.of(
            ImmutableMap.of(
                Currency.GBP, DoubleArray.of(20, 21),
                Currency.EUR, DoubleArray.of(40, 43, 44))),
        "Arrays must have the same size.*");
  }

  public void test_getValues() {
    Map<Currency, DoubleArray> expected = ImmutableMap.of(
        Currency.GBP, DoubleArray.of(20, 21, 22),
        Currency.USD, DoubleArray.of(30, 32, 33),
        Currency.EUR, DoubleArray.of(40, 43, 44));
    assertThat(VALUES_ARRAY.getValues()).isEqualTo(expected);
  }

  public void test_get() {
    MultiCurrencyAmount expected = MultiCurrencyAmount.of(
        CurrencyAmount.of(Currency.GBP, 22),
        CurrencyAmount.of(Currency.USD, 33),
        CurrencyAmount.of(Currency.EUR, 44));
    assertThat(VALUES_ARRAY.get(2)).isEqualTo(expected);
    assertThrows(() -> VALUES_ARRAY.get(3), IndexOutOfBoundsException.class);
    assertThrows(() -> VALUES_ARRAY.get(-1), IndexOutOfBoundsException.class);
  }

  public void test_stream() {
    List<MultiCurrencyAmount> expected = ImmutableList.of(
        MultiCurrencyAmount.of(
            CurrencyAmount.of(Currency.GBP, 20),
            CurrencyAmount.of(Currency.USD, 30),
            CurrencyAmount.of(Currency.EUR, 40)),
        MultiCurrencyAmount.of(
            CurrencyAmount.of(Currency.GBP, 21),
            CurrencyAmount.of(Currency.USD, 32),
            CurrencyAmount.of(Currency.EUR, 43)),
        MultiCurrencyAmount.of(
            CurrencyAmount.of(Currency.GBP, 22),
            CurrencyAmount.of(Currency.USD, 33),
            CurrencyAmount.of(Currency.EUR, 44)));

    assertThat(VALUES_ARRAY.stream().collect(toList())).isEqualTo(expected);
  }

  public void test_convertedTo() {
    FxMatrix fxMatrix = FxMatrix.builder()
        .addRate(GBP, CAD, 2)
        .addRate(USD, CAD, 1.3)
        .addRate(EUR, CAD, 1.4)
        .build();
    CurrencyAmountArray convertedArray = VALUES_ARRAY.convertedTo(Currency.CAD, fxMatrix);
    DoubleArray expected = DoubleArray.of(
        20 * 2 + 30 * 1.3 + 40 * 1.4,
        21 * 2 + 32 * 1.3 + 43 * 1.4,
        22 * 2 + 33 * 1.3 + 44 * 1.4);
    assertThat(convertedArray.getValues()).isEqualTo(expected);
  }

  public void test_convertedTo_existingCurrency() {
    FxMatrix fxMatrix = FxMatrix.builder()
        .addRate(USD, GBP, 1 / 1.5)
        .addRate(EUR, GBP, 0.7)
        .build();
    CurrencyAmountArray convertedArray = VALUES_ARRAY.convertedTo(Currency.GBP, fxMatrix);
    assertThat(convertedArray.getCurrency()).isEqualTo(Currency.GBP);
    double[] expected = new double[]{
        20 + 30 / 1.5 + 40 * 0.7,
        21 + 32 / 1.5 + 43 * 0.7,
        22 + 33 / 1.5 + 44 * 0.7};

    for (int i = 0; i < 3; i++) {
      assertThat(convertedArray.get(i).getAmount()).isEqualTo(expected[i], offset(1e-6));
    }
  }

  //-------------------------------------------------------------------------
  // Test hand-written equals and hashCode methods which correctly handle maps with array values
  public void test_equalsHashCode() {
    MultiCurrencyAmountArray array =
        MultiCurrencyAmountArray.of(
            ImmutableList.of(
                MultiCurrencyAmount.of(
                    CurrencyAmount.of(Currency.GBP, 20),
                    CurrencyAmount.of(Currency.USD, 30),
                    CurrencyAmount.of(Currency.EUR, 40)),
                MultiCurrencyAmount.of(
                    CurrencyAmount.of(Currency.GBP, 21),
                    CurrencyAmount.of(Currency.USD, 32),
                    CurrencyAmount.of(Currency.EUR, 43)),
                MultiCurrencyAmount.of(
                    CurrencyAmount.of(Currency.GBP, 22),
                    CurrencyAmount.of(Currency.USD, 33),
                    CurrencyAmount.of(Currency.EUR, 44))));
    assertThat(array).isEqualTo(VALUES_ARRAY);
    assertThat(array.hashCode()).isEqualTo(VALUES_ARRAY.hashCode());
  }

  //-------------------------------------------------------------------------
  public void coverage() {
    coverImmutableBean(VALUES_ARRAY);
    MultiCurrencyAmountArray test2 = MultiCurrencyAmountArray.of(
        MultiCurrencyAmount.of(
            CurrencyAmount.of(Currency.GBP, 21),
            CurrencyAmount.of(Currency.USD, 31),
            CurrencyAmount.of(Currency.EUR, 41)),
        MultiCurrencyAmount.of(
            CurrencyAmount.of(Currency.GBP, 22),
            CurrencyAmount.of(Currency.USD, 33),
            CurrencyAmount.of(Currency.EUR, 44)));
    coverBeanEquals(VALUES_ARRAY, test2);
  }

  //-------------------------------------------------------------------------
  public void test_plusArray() {
    MultiCurrencyAmountArray array1 = MultiCurrencyAmountArray.of(
        ImmutableMap.of(
            Currency.USD, DoubleArray.of(30, 32, 33),
            Currency.EUR, DoubleArray.of(40, 43, 44),
            Currency.CHF, DoubleArray.of(50, 54, 56)));
    MultiCurrencyAmountArray array2 = MultiCurrencyAmountArray.of(
        ImmutableMap.of(
            Currency.GBP, DoubleArray.of(20, 21, 22),
            Currency.EUR, DoubleArray.of(140, 143, 144),
            Currency.CHF, DoubleArray.of(250, 254, 256)));
    MultiCurrencyAmountArray expected = MultiCurrencyAmountArray.of(
        ImmutableMap.of(
            Currency.GBP, DoubleArray.of(20, 21, 22),
            Currency.USD, DoubleArray.of(30, 32, 33),
            Currency.EUR, DoubleArray.of(180, 186, 188),
            Currency.CHF, DoubleArray.of(300, 308, 312)));

    assertThat(array1.plus(array2)).isEqualTo(expected);
  }

  public void test_plusAmount() {
    MultiCurrencyAmountArray array = MultiCurrencyAmountArray.of(
        ImmutableMap.of(
            Currency.USD, DoubleArray.of(30, 32, 33),
            Currency.EUR, DoubleArray.of(40, 43, 44),
            Currency.CHF, DoubleArray.of(50, 54, 56)));
    MultiCurrencyAmount amount = MultiCurrencyAmount.of(
        ImmutableMap.of(
            Currency.GBP, 21d,
            Currency.EUR, 143d,
            Currency.CHF, 254d));
    MultiCurrencyAmountArray expected = MultiCurrencyAmountArray.of(
        ImmutableMap.of(
            Currency.GBP, DoubleArray.of(21, 21, 21),
            Currency.USD, DoubleArray.of(30, 32, 33),
            Currency.EUR, DoubleArray.of(183, 186, 187),
            Currency.CHF, DoubleArray.of(304, 308, 310)));

    assertThat(array.plus(amount)).isEqualTo(expected);
  }

  public void test_plusDifferentSize() {
    MultiCurrencyAmountArray array1 = MultiCurrencyAmountArray.of(
        ImmutableMap.of(
            Currency.USD, DoubleArray.of(30, 32),
            Currency.EUR, DoubleArray.of(40, 43),
            Currency.CHF, DoubleArray.of(50, 54)));
    MultiCurrencyAmountArray array2 = MultiCurrencyAmountArray.of(
        ImmutableMap.of(
            Currency.GBP, DoubleArray.of(20, 21, 22),
            Currency.EUR, DoubleArray.of(140, 143, 144),
            Currency.CHF, DoubleArray.of(250, 254, 256)));

    assertThrowsIllegalArg(() -> array1.plus(array2));
  }

  public void test_minusArray() {
    MultiCurrencyAmountArray array1 = MultiCurrencyAmountArray.of(
        ImmutableMap.of(
            Currency.USD, DoubleArray.of(30, 32, 33),
            Currency.EUR, DoubleArray.of(40, 43, 44),
            Currency.CHF, DoubleArray.of(50, 54, 56)));
    MultiCurrencyAmountArray array2 = MultiCurrencyAmountArray.of(
        ImmutableMap.of(
            Currency.GBP, DoubleArray.of(20, 21, 22),
            Currency.EUR, DoubleArray.of(140, 143, 144),
            Currency.CHF, DoubleArray.of(250, 254, 256)));
    MultiCurrencyAmountArray expected = MultiCurrencyAmountArray.of(
        ImmutableMap.of(
            Currency.GBP, DoubleArray.of(-20, -21, -22),
            Currency.USD, DoubleArray.of(30, 32, 33),
            Currency.EUR, DoubleArray.of(-100, -100, -100),
            Currency.CHF, DoubleArray.of(-200, -200, -200)));

    assertThat(array1.minus(array2)).isEqualTo(expected);
  }

  public void test_minusAmount() {
    MultiCurrencyAmountArray array = MultiCurrencyAmountArray.of(
        ImmutableMap.of(
            Currency.USD, DoubleArray.of(30, 32, 33),
            Currency.EUR, DoubleArray.of(40, 43, 44),
            Currency.CHF, DoubleArray.of(50, 54, 56)));
    MultiCurrencyAmount amount = MultiCurrencyAmount.of(
        ImmutableMap.of(
            Currency.GBP, 21d,
            Currency.EUR, 143d,
            Currency.CHF, 254d));
    MultiCurrencyAmountArray expected = MultiCurrencyAmountArray.of(
        ImmutableMap.of(
            Currency.GBP, DoubleArray.of(-21, -21, -21),
            Currency.USD, DoubleArray.of(30, 32, 33),
            Currency.EUR, DoubleArray.of(-103, -100, -99),
            Currency.CHF, DoubleArray.of(-204, -200, -198)));

    assertThat(array.minus(amount)).isEqualTo(expected);
  }

  public void test_minusDifferentSize() {
    MultiCurrencyAmountArray array1 = MultiCurrencyAmountArray.of(
      ImmutableMap.of(
          Currency.USD, DoubleArray.of(30, 32),
          Currency.EUR, DoubleArray.of(40, 43),
          Currency.CHF, DoubleArray.of(50, 54)));
    MultiCurrencyAmountArray array2 = MultiCurrencyAmountArray.of(
        ImmutableMap.of(
            Currency.GBP, DoubleArray.of(20, 21, 22),
            Currency.EUR, DoubleArray.of(140, 143, 144),
            Currency.CHF, DoubleArray.of(250, 254, 256)));

    assertThrowsIllegalArg(() -> array1.minus(array2));
  }

}
