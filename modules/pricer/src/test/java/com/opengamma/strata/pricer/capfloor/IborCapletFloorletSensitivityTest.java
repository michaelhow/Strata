/**
 * Copyright (C) 2016 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.strata.pricer.capfloor;

import static com.opengamma.strata.basics.currency.Currency.GBP;
import static com.opengamma.strata.basics.currency.Currency.USD;
import static com.opengamma.strata.basics.index.IborIndices.GBP_LIBOR_3M;
import static com.opengamma.strata.basics.index.IborIndices.USD_LIBOR_3M;
import static com.opengamma.strata.collect.TestHelper.assertSerialization;
import static com.opengamma.strata.collect.TestHelper.coverBeanEquals;
import static com.opengamma.strata.collect.TestHelper.coverImmutableBean;
import static com.opengamma.strata.collect.TestHelper.dateUtc;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertSame;

import java.time.ZonedDateTime;

import org.testng.annotations.Test;

import com.google.common.collect.ImmutableList;
import com.opengamma.strata.basics.currency.CurrencyPair;
import com.opengamma.strata.basics.currency.FxMatrix;
import com.opengamma.strata.market.sensitivity.MutablePointSensitivities;
import com.opengamma.strata.market.sensitivity.PointSensitivities;
import com.opengamma.strata.pricer.ZeroRateSensitivity;

/**
 * Test {@link IborCapFloorSensitivity}.
 */
@Test
public class IborCapletFloorletSensitivityTest {

  private static final ZonedDateTime EXPIRY_DATE = dateUtc(2015, 8, 27);
  private static final double FORWARD = 0.015d;
  private static final double STRIKE = 0.001d;
  private static final double SENSITIVITY = 0.54d;

  public void test_of_noCurrency() {
    IborCapFloorSensitivity test =
        IborCapFloorSensitivity.of(GBP_LIBOR_3M, EXPIRY_DATE, STRIKE, FORWARD, SENSITIVITY);
    assertEquals(test.getIndex(), GBP_LIBOR_3M);
    assertEquals(test.getCurrency(), GBP);
    assertEquals(test.getExpiry(), EXPIRY_DATE);
    assertEquals(test.getStrike(), STRIKE);
    assertEquals(test.getForward(), FORWARD);
    assertEquals(test.getSensitivity(), SENSITIVITY);
  }

  public void test_of_withCurrency() {
    IborCapFloorSensitivity test =
        IborCapFloorSensitivity.of(USD_LIBOR_3M, EXPIRY_DATE, STRIKE, FORWARD, GBP, SENSITIVITY);
    assertEquals(test.getIndex(), USD_LIBOR_3M);
    assertEquals(test.getCurrency(), GBP);
    assertEquals(test.getExpiry(), EXPIRY_DATE);
    assertEquals(test.getStrike(), STRIKE);
    assertEquals(test.getForward(), FORWARD);
    assertEquals(test.getSensitivity(), SENSITIVITY);
  }

  //-------------------------------------------------------------------------
  public void test_withCurrency() {
    IborCapFloorSensitivity base =
        IborCapFloorSensitivity.of(GBP_LIBOR_3M, EXPIRY_DATE, STRIKE, FORWARD, SENSITIVITY);
    assertSame(base.withCurrency(GBP), base);
    IborCapFloorSensitivity expected =
        IborCapFloorSensitivity.of(GBP_LIBOR_3M, EXPIRY_DATE, STRIKE, FORWARD, USD, SENSITIVITY);
    IborCapFloorSensitivity test = base.withCurrency(USD);
    assertEquals(test, expected);
  }

  //-------------------------------------------------------------------------
  public void test_withSensitivity() {
    IborCapFloorSensitivity base =
        IborCapFloorSensitivity.of(USD_LIBOR_3M, EXPIRY_DATE, STRIKE, FORWARD, SENSITIVITY);
    double sensi = 23.5;
    IborCapFloorSensitivity expected =
        IborCapFloorSensitivity.of(USD_LIBOR_3M, EXPIRY_DATE, STRIKE, FORWARD, sensi);
    IborCapFloorSensitivity test = base.withSensitivity(sensi);
    assertEquals(test, expected);
  }

  //-------------------------------------------------------------------------
  public void test_compareKey() {
    IborCapFloorSensitivity a1 =
        IborCapFloorSensitivity.of(GBP_LIBOR_3M, EXPIRY_DATE, STRIKE, FORWARD, GBP, SENSITIVITY);
    IborCapFloorSensitivity a2 =
        IborCapFloorSensitivity.of(GBP_LIBOR_3M, EXPIRY_DATE, STRIKE, FORWARD, GBP, SENSITIVITY);
    IborCapFloorSensitivity b =
        IborCapFloorSensitivity.of(USD_LIBOR_3M, EXPIRY_DATE, STRIKE, FORWARD, GBP, SENSITIVITY);
    IborCapFloorSensitivity c =
        IborCapFloorSensitivity.of(GBP_LIBOR_3M, EXPIRY_DATE.plusDays(2), STRIKE, FORWARD, GBP, SENSITIVITY);
    IborCapFloorSensitivity d =
        IborCapFloorSensitivity.of(GBP_LIBOR_3M, EXPIRY_DATE, 0.009, FORWARD, GBP, SENSITIVITY);
    IborCapFloorSensitivity e =
        IborCapFloorSensitivity.of(GBP_LIBOR_3M, EXPIRY_DATE, STRIKE, 0.005, GBP, SENSITIVITY);
    IborCapFloorSensitivity f =
        IborCapFloorSensitivity.of(GBP_LIBOR_3M, EXPIRY_DATE, STRIKE, FORWARD, USD, SENSITIVITY);
    ZeroRateSensitivity other = ZeroRateSensitivity.of(GBP, 2d, 32d);
    assertEquals(a1.compareKey(a2), 0);
    assertEquals(a1.compareKey(b) < 0, true);
    assertEquals(b.compareKey(a1) > 0, true);
    assertEquals(a1.compareKey(c) < 0, true);
    assertEquals(c.compareKey(a1) > 0, true);
    assertEquals(a1.compareKey(d) < 0, true);
    assertEquals(d.compareKey(a1) > 0, true);
    assertEquals(a1.compareKey(e) > 0, true);
    assertEquals(e.compareKey(a1) < 0, true);
    assertEquals(a1.compareKey(f) < 0, true);
    assertEquals(f.compareKey(a1) > 0, true);
    assertEquals(a1.compareKey(other) < 0, true);
    assertEquals(other.compareKey(a1) > 0, true);
  }

  //-------------------------------------------------------------------------
  public void test_convertedTo() {
    IborCapFloorSensitivity base =
        IborCapFloorSensitivity.of(GBP_LIBOR_3M, EXPIRY_DATE, STRIKE, FORWARD, GBP, SENSITIVITY);
    double rate = 1.5d;
    FxMatrix matrix = FxMatrix.of(CurrencyPair.of(GBP, USD), rate);
    IborCapFloorSensitivity test1 = base.convertedTo(USD, matrix);
    IborCapFloorSensitivity expected =
        IborCapFloorSensitivity.of(GBP_LIBOR_3M, EXPIRY_DATE, STRIKE, FORWARD, USD, SENSITIVITY * rate);
    assertEquals(test1, expected);
    IborCapFloorSensitivity test2 = base.convertedTo(GBP, matrix);
    assertEquals(test2, base);
  }

  //-------------------------------------------------------------------------
  public void test_multipliedBy() {
    IborCapFloorSensitivity base =
        IborCapFloorSensitivity.of(GBP_LIBOR_3M, EXPIRY_DATE, STRIKE, FORWARD, GBP, SENSITIVITY);
    double factor = 3.5d;
    IborCapFloorSensitivity expected =
        IborCapFloorSensitivity.of(GBP_LIBOR_3M, EXPIRY_DATE, STRIKE, FORWARD, GBP, SENSITIVITY * factor);
    IborCapFloorSensitivity test = base.multipliedBy(3.5d);
    assertEquals(test, expected);
  }

  //-------------------------------------------------------------------------
  public void test_mapSensitivity() {
    IborCapFloorSensitivity base =
        IborCapFloorSensitivity.of(GBP_LIBOR_3M, EXPIRY_DATE, STRIKE, FORWARD, GBP, SENSITIVITY);
    IborCapFloorSensitivity expected =
        IborCapFloorSensitivity.of(GBP_LIBOR_3M, EXPIRY_DATE, STRIKE, FORWARD, GBP, 1d / SENSITIVITY);
    IborCapFloorSensitivity test = base.mapSensitivity(s -> 1 / s);
    assertEquals(test, expected);
  }

  //-------------------------------------------------------------------------
  public void test_normalize() {
    IborCapFloorSensitivity base =
        IborCapFloorSensitivity.of(GBP_LIBOR_3M, EXPIRY_DATE, STRIKE, FORWARD, GBP, SENSITIVITY);
    IborCapFloorSensitivity test = base.normalize();
    assertSame(test, base);
  }

  //-------------------------------------------------------------------------
  public void test_buildInto() {
    IborCapFloorSensitivity base =
        IborCapFloorSensitivity.of(GBP_LIBOR_3M, EXPIRY_DATE, STRIKE, FORWARD, GBP, SENSITIVITY);
    MutablePointSensitivities combo = new MutablePointSensitivities();
    MutablePointSensitivities test = base.buildInto(combo);
    assertSame(test, combo);
    assertEquals(test.getSensitivities(), ImmutableList.of(base));
  }

  //-------------------------------------------------------------------------
  public void test_build() {
    IborCapFloorSensitivity base =
        IborCapFloorSensitivity.of(GBP_LIBOR_3M, EXPIRY_DATE, STRIKE, FORWARD, GBP, SENSITIVITY);
    PointSensitivities test = base.build();
    assertEquals(test.getSensitivities(), ImmutableList.of(base));
  }

  //-------------------------------------------------------------------------
  public void test_cloned() {
    IborCapFloorSensitivity base =
        IborCapFloorSensitivity.of(GBP_LIBOR_3M, EXPIRY_DATE, STRIKE, FORWARD, GBP, SENSITIVITY);
    IborCapFloorSensitivity test = base.cloned();
    assertSame(test, base);
  }

  //-------------------------------------------------------------------------
  public void coverage() {
    IborCapFloorSensitivity test1 =
        IborCapFloorSensitivity.of(GBP_LIBOR_3M, EXPIRY_DATE, STRIKE, FORWARD, GBP, SENSITIVITY);
    coverImmutableBean(test1);
    IborCapFloorSensitivity test2 =
        IborCapFloorSensitivity.of(USD_LIBOR_3M, dateUtc(2015, 8, 28), 0.98, 0.99, 32d);
    coverBeanEquals(test1, test2);
  }

  public void test_serialization() {
    IborCapFloorSensitivity test =
        IborCapFloorSensitivity.of(GBP_LIBOR_3M, EXPIRY_DATE, STRIKE, FORWARD, GBP, SENSITIVITY);
    assertSerialization(test);
  }

}
