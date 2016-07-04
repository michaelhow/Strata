/**
 * Copyright (C) 2016 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.strata.pricer.capfloor;

import com.opengamma.strata.market.ValueType;
import com.opengamma.strata.market.param.ParameterPerturbation;

/**
 * Volatility for Ibor cap/floor in the log-normal or Black model.
 */
public interface BlackIborCapFloorVolatilities
    extends IborCapFloorVolatilities {

  @Override
  public default ValueType getVolatilityType() {
    return ValueType.BLACK_VOLATILITY;
  }

  @Override
  public abstract BlackIborCapFloorVolatilities withParameter(int parameterIndex, double newValue);

  @Override
  public abstract BlackIborCapFloorVolatilities withPerturbation(ParameterPerturbation perturbation);

}
