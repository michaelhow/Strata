/**
 * Copyright (C) 2015 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.strata.pricer.swaption;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.function.DoubleUnaryOperator;

import org.joda.beans.Bean;
import org.joda.beans.BeanBuilder;
import org.joda.beans.BeanDefinition;
import org.joda.beans.ImmutableBean;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.Property;
import org.joda.beans.PropertyDefinition;
import org.joda.beans.impl.direct.DirectFieldsBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaBean;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

import com.google.common.collect.ComparisonChain;
import com.opengamma.strata.basics.currency.Currency;
import com.opengamma.strata.basics.currency.FxRateProvider;
import com.opengamma.strata.market.model.SabrParameterType;
import com.opengamma.strata.market.sensitivity.MutablePointSensitivities;
import com.opengamma.strata.market.sensitivity.PointSensitivity;
import com.opengamma.strata.market.sensitivity.PointSensitivityBuilder;
import com.opengamma.strata.product.swap.type.FixedIborSwapConvention;

/**
 * Sensitivity of a swaption to SABR model parameters.
 * <p>
 * Holds the sensitivity vales to grid points of the SABR parameters.
 */
@BeanDefinition(builderScope = "private")
public final class SwaptionSabrSensitivity
    implements PointSensitivity, PointSensitivityBuilder, ImmutableBean, Serializable {

  /**
  * The convention of the swap for which the data is valid.
  */
  @PropertyDefinition(validate = "notNull")
  private final FixedIborSwapConvention convention;
  /**
  * The expiry date/time of the option.
  */
  @PropertyDefinition(validate = "notNull")
  private final ZonedDateTime expiry;
  /**
  * The underlying swap tenor.
  */
  @PropertyDefinition
  private final double tenor;
  /**
   * The type of the sensitivity.
   */
  @PropertyDefinition
  private final SabrParameterType sensitivityType;
  /**
  * The currency of the sensitivity.
  */
  @PropertyDefinition(overrideGet = true)
  private final Currency currency;
  /**
   * The value of the sensitivity.
   */
  @PropertyDefinition(overrideGet = true)
  private final double sensitivity;

  //-------------------------------------------------------------------------
  /**
   * Obtains an instance from the specified elements.
   * 
   * @param convention  the convention of the swap for which the data is valid
   * @param expiry  the expiry date/time of the option
   * @param tenor  the underlying swap tenor
   * @param sensitivityType  the type of the sensitivity
   * @param sensitivityCurrency  the currency of the sensitivity
   * @param sensitivity  the value of the sensitivity
   * @return the sensitivity object
   */
  public static SwaptionSabrSensitivity of(
      FixedIborSwapConvention convention,
      ZonedDateTime expiry,
      double tenor,
      SabrParameterType sensitivityType,
      Currency sensitivityCurrency,
      double sensitivity) {

    return new SwaptionSabrSensitivity(
        convention,
        expiry,
        tenor,
        sensitivityType,
        sensitivityCurrency,
        sensitivity);
  }

  //-------------------------------------------------------------------------
  @Override
  public SwaptionSabrSensitivity withCurrency(Currency currency) {
    if (this.currency.equals(currency)) {
      return this;
    }
    return new SwaptionSabrSensitivity(convention, expiry, tenor, sensitivityType, currency, sensitivity);
  }

  @Override
  public SwaptionSabrSensitivity withSensitivity(double sensitivity) {
    return new SwaptionSabrSensitivity(convention, expiry, tenor, sensitivityType, currency, sensitivity);
  }

  @Override
  public int compareKey(PointSensitivity other) {
    if (other instanceof SwaptionSabrSensitivity) {
      SwaptionSabrSensitivity otherSwpt = (SwaptionSabrSensitivity) other;
      return ComparisonChain.start()
          .compare(currency, otherSwpt.currency)
          .compare(expiry, otherSwpt.expiry)
          .compare(tenor, otherSwpt.tenor)
          .compare(sensitivityType, otherSwpt.sensitivityType)
          .compare(convention.toString(), otherSwpt.convention.toString())
          .result();
    }
    return getClass().getSimpleName().compareTo(other.getClass().getSimpleName());
  }

  @Override
  public SwaptionSabrSensitivity convertedTo(Currency resultCurrency, FxRateProvider rateProvider) {
    return (SwaptionSabrSensitivity) PointSensitivity.super.convertedTo(resultCurrency, rateProvider);
  }

  //-------------------------------------------------------------------------
  @Override
  public SwaptionSabrSensitivity multipliedBy(double factor) {
    return new SwaptionSabrSensitivity(convention, expiry, tenor, sensitivityType, currency, sensitivity * factor);
  }

  @Override
  public SwaptionSabrSensitivity mapSensitivity(DoubleUnaryOperator operator) {
    return new SwaptionSabrSensitivity(
        convention, expiry, tenor, sensitivityType, currency, operator.applyAsDouble(sensitivity));
  }

  @Override
  public SwaptionSabrSensitivity normalize() {
    return this;
  }

  @Override
  public MutablePointSensitivities buildInto(MutablePointSensitivities combination) {
    return combination.add(this);
  }

  @Override
  public SwaptionSabrSensitivity cloned() {
    return this;
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code SwaptionSabrSensitivity}.
   * @return the meta-bean, not null
   */
  public static SwaptionSabrSensitivity.Meta meta() {
    return SwaptionSabrSensitivity.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(SwaptionSabrSensitivity.Meta.INSTANCE);
  }

  /**
   * The serialization version id.
   */
  private static final long serialVersionUID = 1L;

  private SwaptionSabrSensitivity(
      FixedIborSwapConvention convention,
      ZonedDateTime expiry,
      double tenor,
      SabrParameterType sensitivityType,
      Currency currency,
      double sensitivity) {
    JodaBeanUtils.notNull(convention, "convention");
    JodaBeanUtils.notNull(expiry, "expiry");
    this.convention = convention;
    this.expiry = expiry;
    this.tenor = tenor;
    this.sensitivityType = sensitivityType;
    this.currency = currency;
    this.sensitivity = sensitivity;
  }

  @Override
  public SwaptionSabrSensitivity.Meta metaBean() {
    return SwaptionSabrSensitivity.Meta.INSTANCE;
  }

  @Override
  public <R> Property<R> property(String propertyName) {
    return metaBean().<R>metaProperty(propertyName).createProperty(this);
  }

  @Override
  public Set<String> propertyNames() {
    return metaBean().metaPropertyMap().keySet();
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the convention of the swap for which the data is valid.
   * @return the value of the property, not null
   */
  public FixedIborSwapConvention getConvention() {
    return convention;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the expiry date/time of the option.
   * @return the value of the property, not null
   */
  public ZonedDateTime getExpiry() {
    return expiry;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the underlying swap tenor.
   * @return the value of the property
   */
  public double getTenor() {
    return tenor;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the type of the sensitivity.
   * @return the value of the property
   */
  public SabrParameterType getSensitivityType() {
    return sensitivityType;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the currency of the sensitivity.
   * @return the value of the property
   */
  @Override
  public Currency getCurrency() {
    return currency;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the value of the sensitivity.
   * @return the value of the property
   */
  @Override
  public double getSensitivity() {
    return sensitivity;
  }

  //-----------------------------------------------------------------------
  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      SwaptionSabrSensitivity other = (SwaptionSabrSensitivity) obj;
      return JodaBeanUtils.equal(convention, other.convention) &&
          JodaBeanUtils.equal(expiry, other.expiry) &&
          JodaBeanUtils.equal(tenor, other.tenor) &&
          JodaBeanUtils.equal(sensitivityType, other.sensitivityType) &&
          JodaBeanUtils.equal(currency, other.currency) &&
          JodaBeanUtils.equal(sensitivity, other.sensitivity);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = getClass().hashCode();
    hash = hash * 31 + JodaBeanUtils.hashCode(convention);
    hash = hash * 31 + JodaBeanUtils.hashCode(expiry);
    hash = hash * 31 + JodaBeanUtils.hashCode(tenor);
    hash = hash * 31 + JodaBeanUtils.hashCode(sensitivityType);
    hash = hash * 31 + JodaBeanUtils.hashCode(currency);
    hash = hash * 31 + JodaBeanUtils.hashCode(sensitivity);
    return hash;
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(224);
    buf.append("SwaptionSabrSensitivity{");
    buf.append("convention").append('=').append(convention).append(',').append(' ');
    buf.append("expiry").append('=').append(expiry).append(',').append(' ');
    buf.append("tenor").append('=').append(tenor).append(',').append(' ');
    buf.append("sensitivityType").append('=').append(sensitivityType).append(',').append(' ');
    buf.append("currency").append('=').append(currency).append(',').append(' ');
    buf.append("sensitivity").append('=').append(JodaBeanUtils.toString(sensitivity));
    buf.append('}');
    return buf.toString();
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code SwaptionSabrSensitivity}.
   */
  public static final class Meta extends DirectMetaBean {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code convention} property.
     */
    private final MetaProperty<FixedIborSwapConvention> convention = DirectMetaProperty.ofImmutable(
        this, "convention", SwaptionSabrSensitivity.class, FixedIborSwapConvention.class);
    /**
     * The meta-property for the {@code expiry} property.
     */
    private final MetaProperty<ZonedDateTime> expiry = DirectMetaProperty.ofImmutable(
        this, "expiry", SwaptionSabrSensitivity.class, ZonedDateTime.class);
    /**
     * The meta-property for the {@code tenor} property.
     */
    private final MetaProperty<Double> tenor = DirectMetaProperty.ofImmutable(
        this, "tenor", SwaptionSabrSensitivity.class, Double.TYPE);
    /**
     * The meta-property for the {@code sensitivityType} property.
     */
    private final MetaProperty<SabrParameterType> sensitivityType = DirectMetaProperty.ofImmutable(
        this, "sensitivityType", SwaptionSabrSensitivity.class, SabrParameterType.class);
    /**
     * The meta-property for the {@code currency} property.
     */
    private final MetaProperty<Currency> currency = DirectMetaProperty.ofImmutable(
        this, "currency", SwaptionSabrSensitivity.class, Currency.class);
    /**
     * The meta-property for the {@code sensitivity} property.
     */
    private final MetaProperty<Double> sensitivity = DirectMetaProperty.ofImmutable(
        this, "sensitivity", SwaptionSabrSensitivity.class, Double.TYPE);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> metaPropertyMap$ = new DirectMetaPropertyMap(
        this, null,
        "convention",
        "expiry",
        "tenor",
        "sensitivityType",
        "currency",
        "sensitivity");

    /**
     * Restricted constructor.
     */
    private Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case 2039569265:  // convention
          return convention;
        case -1289159373:  // expiry
          return expiry;
        case 110246592:  // tenor
          return tenor;
        case 1598929529:  // sensitivityType
          return sensitivityType;
        case 575402001:  // currency
          return currency;
        case 564403871:  // sensitivity
          return sensitivity;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends SwaptionSabrSensitivity> builder() {
      return new SwaptionSabrSensitivity.Builder();
    }

    @Override
    public Class<? extends SwaptionSabrSensitivity> beanType() {
      return SwaptionSabrSensitivity.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code convention} property.
     * @return the meta-property, not null
     */
    public MetaProperty<FixedIborSwapConvention> convention() {
      return convention;
    }

    /**
     * The meta-property for the {@code expiry} property.
     * @return the meta-property, not null
     */
    public MetaProperty<ZonedDateTime> expiry() {
      return expiry;
    }

    /**
     * The meta-property for the {@code tenor} property.
     * @return the meta-property, not null
     */
    public MetaProperty<Double> tenor() {
      return tenor;
    }

    /**
     * The meta-property for the {@code sensitivityType} property.
     * @return the meta-property, not null
     */
    public MetaProperty<SabrParameterType> sensitivityType() {
      return sensitivityType;
    }

    /**
     * The meta-property for the {@code currency} property.
     * @return the meta-property, not null
     */
    public MetaProperty<Currency> currency() {
      return currency;
    }

    /**
     * The meta-property for the {@code sensitivity} property.
     * @return the meta-property, not null
     */
    public MetaProperty<Double> sensitivity() {
      return sensitivity;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 2039569265:  // convention
          return ((SwaptionSabrSensitivity) bean).getConvention();
        case -1289159373:  // expiry
          return ((SwaptionSabrSensitivity) bean).getExpiry();
        case 110246592:  // tenor
          return ((SwaptionSabrSensitivity) bean).getTenor();
        case 1598929529:  // sensitivityType
          return ((SwaptionSabrSensitivity) bean).getSensitivityType();
        case 575402001:  // currency
          return ((SwaptionSabrSensitivity) bean).getCurrency();
        case 564403871:  // sensitivity
          return ((SwaptionSabrSensitivity) bean).getSensitivity();
      }
      return super.propertyGet(bean, propertyName, quiet);
    }

    @Override
    protected void propertySet(Bean bean, String propertyName, Object newValue, boolean quiet) {
      metaProperty(propertyName);
      if (quiet) {
        return;
      }
      throw new UnsupportedOperationException("Property cannot be written: " + propertyName);
    }

  }

  //-----------------------------------------------------------------------
  /**
   * The bean-builder for {@code SwaptionSabrSensitivity}.
   */
  private static final class Builder extends DirectFieldsBeanBuilder<SwaptionSabrSensitivity> {

    private FixedIborSwapConvention convention;
    private ZonedDateTime expiry;
    private double tenor;
    private SabrParameterType sensitivityType;
    private Currency currency;
    private double sensitivity;

    /**
     * Restricted constructor.
     */
    private Builder() {
    }

    //-----------------------------------------------------------------------
    @Override
    public Object get(String propertyName) {
      switch (propertyName.hashCode()) {
        case 2039569265:  // convention
          return convention;
        case -1289159373:  // expiry
          return expiry;
        case 110246592:  // tenor
          return tenor;
        case 1598929529:  // sensitivityType
          return sensitivityType;
        case 575402001:  // currency
          return currency;
        case 564403871:  // sensitivity
          return sensitivity;
        default:
          throw new NoSuchElementException("Unknown property: " + propertyName);
      }
    }

    @Override
    public Builder set(String propertyName, Object newValue) {
      switch (propertyName.hashCode()) {
        case 2039569265:  // convention
          this.convention = (FixedIborSwapConvention) newValue;
          break;
        case -1289159373:  // expiry
          this.expiry = (ZonedDateTime) newValue;
          break;
        case 110246592:  // tenor
          this.tenor = (Double) newValue;
          break;
        case 1598929529:  // sensitivityType
          this.sensitivityType = (SabrParameterType) newValue;
          break;
        case 575402001:  // currency
          this.currency = (Currency) newValue;
          break;
        case 564403871:  // sensitivity
          this.sensitivity = (Double) newValue;
          break;
        default:
          throw new NoSuchElementException("Unknown property: " + propertyName);
      }
      return this;
    }

    @Override
    public Builder set(MetaProperty<?> property, Object value) {
      super.set(property, value);
      return this;
    }

    @Override
    public Builder setString(String propertyName, String value) {
      setString(meta().metaProperty(propertyName), value);
      return this;
    }

    @Override
    public Builder setString(MetaProperty<?> property, String value) {
      super.setString(property, value);
      return this;
    }

    @Override
    public Builder setAll(Map<String, ? extends Object> propertyValueMap) {
      super.setAll(propertyValueMap);
      return this;
    }

    @Override
    public SwaptionSabrSensitivity build() {
      return new SwaptionSabrSensitivity(
          convention,
          expiry,
          tenor,
          sensitivityType,
          currency,
          sensitivity);
    }

    //-----------------------------------------------------------------------
    @Override
    public String toString() {
      StringBuilder buf = new StringBuilder(224);
      buf.append("SwaptionSabrSensitivity.Builder{");
      buf.append("convention").append('=').append(JodaBeanUtils.toString(convention)).append(',').append(' ');
      buf.append("expiry").append('=').append(JodaBeanUtils.toString(expiry)).append(',').append(' ');
      buf.append("tenor").append('=').append(JodaBeanUtils.toString(tenor)).append(',').append(' ');
      buf.append("sensitivityType").append('=').append(JodaBeanUtils.toString(sensitivityType)).append(',').append(' ');
      buf.append("currency").append('=').append(JodaBeanUtils.toString(currency)).append(',').append(' ');
      buf.append("sensitivity").append('=').append(JodaBeanUtils.toString(sensitivity));
      buf.append('}');
      return buf.toString();
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
