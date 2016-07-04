/**
 * Copyright (C) 2016 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.strata.pricer.capfloor;

import com.opengamma.strata.market.ValueType;
import com.opengamma.strata.market.param.ParameterPerturbation;

/**
 * Volatility for Ibor cap/floor in the normal or Bachelier model.
 */
public interface NormalIborCapFloorVolatilities
    extends IborCapFloorVolatilities {

  @Override
  public default ValueType getVolatilityType() {
    return ValueType.NORMAL_VOLATILITY;
  }

  @Override
  public abstract NormalIborCapFloorVolatilities withParameter(int parameterIndex, double newValue);

  @Override
  public abstract NormalIborCapFloorVolatilities withPerturbation(ParameterPerturbation perturbation);

}
