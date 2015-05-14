/**
 * Copyright (C) 2015 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.strata.marketdata.curve;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import org.joda.beans.Bean;
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

import com.google.common.collect.ImmutableSet;
import com.opengamma.analytics.financial.model.interestrate.curve.YieldCurve;
import com.opengamma.strata.basics.market.MarketDataKey;

/**
 * An item in the configuration for a curve group, containing the configuration for a curve and the market data keys
 * identifying how the curve is used.
 * <p>
 * A curve can be used for multiple purposes and therefore the curve itself contains no information about how
 * it is used.
 * <p>
 * In the simple case a curve is only used for a single purpose. For example, if a curve is used for discounting
 * it will have one key of type {@code DiscountingCurveKey}.
 * <p>
 * A single curve can also be used as both a discounting curve and a forward curve.
 * In that case its key set would contain a {@code DiscountingCurveKey} and a {@code RateIndexCurveKey}.
 * <p>
 * Every curve must be associated with at least once key.
 */
@BeanDefinition
public final class CurveGroupEntry implements ImmutableBean {

  /** The configuration of the curve. */
  @PropertyDefinition(validate = "notNull")
  private final CurveConfig curveConfig;

  /** The keys identifying how the curve is used in the group. */
  @PropertyDefinition(validate = "notEmpty")
  private final ImmutableSet<MarketDataKey<YieldCurve>> curveKeys;

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code CurveGroupEntry}.
   * @return the meta-bean, not null
   */
  public static CurveGroupEntry.Meta meta() {
    return CurveGroupEntry.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(CurveGroupEntry.Meta.INSTANCE);
  }

  /**
   * Returns a builder used to create an instance of the bean.
   * @return the builder, not null
   */
  public static CurveGroupEntry.Builder builder() {
    return new CurveGroupEntry.Builder();
  }

  private CurveGroupEntry(
      CurveConfig curveConfig,
      Set<MarketDataKey<YieldCurve>> curveKeys) {
    JodaBeanUtils.notNull(curveConfig, "curveConfig");
    JodaBeanUtils.notEmpty(curveKeys, "curveKeys");
    this.curveConfig = curveConfig;
    this.curveKeys = ImmutableSet.copyOf(curveKeys);
  }

  @Override
  public CurveGroupEntry.Meta metaBean() {
    return CurveGroupEntry.Meta.INSTANCE;
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
   * Gets the configuration of the curve.
   * @return the value of the property, not null
   */
  public CurveConfig getCurveConfig() {
    return curveConfig;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the keys identifying how the curve is used in the group.
   * @return the value of the property, not empty
   */
  public ImmutableSet<MarketDataKey<YieldCurve>> getCurveKeys() {
    return curveKeys;
  }

  //-----------------------------------------------------------------------
  /**
   * Returns a builder that allows this bean to be mutated.
   * @return the mutable builder, not null
   */
  public Builder toBuilder() {
    return new Builder(this);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      CurveGroupEntry other = (CurveGroupEntry) obj;
      return JodaBeanUtils.equal(getCurveConfig(), other.getCurveConfig()) &&
          JodaBeanUtils.equal(getCurveKeys(), other.getCurveKeys());
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = getClass().hashCode();
    hash = hash * 31 + JodaBeanUtils.hashCode(getCurveConfig());
    hash = hash * 31 + JodaBeanUtils.hashCode(getCurveKeys());
    return hash;
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(96);
    buf.append("CurveGroupEntry{");
    buf.append("curveConfig").append('=').append(getCurveConfig()).append(',').append(' ');
    buf.append("curveKeys").append('=').append(JodaBeanUtils.toString(getCurveKeys()));
    buf.append('}');
    return buf.toString();
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code CurveGroupEntry}.
   */
  public static final class Meta extends DirectMetaBean {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code curveConfig} property.
     */
    private final MetaProperty<CurveConfig> curveConfig = DirectMetaProperty.ofImmutable(
        this, "curveConfig", CurveGroupEntry.class, CurveConfig.class);
    /**
     * The meta-property for the {@code curveKeys} property.
     */
    @SuppressWarnings({"unchecked", "rawtypes" })
    private final MetaProperty<ImmutableSet<MarketDataKey<YieldCurve>>> curveKeys = DirectMetaProperty.ofImmutable(
        this, "curveKeys", CurveGroupEntry.class, (Class) ImmutableSet.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> metaPropertyMap$ = new DirectMetaPropertyMap(
        this, null,
        "curveConfig",
        "curveKeys");

    /**
     * Restricted constructor.
     */
    private Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case 2042609937:  // curveConfig
          return curveConfig;
        case 771068803:  // curveKeys
          return curveKeys;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public CurveGroupEntry.Builder builder() {
      return new CurveGroupEntry.Builder();
    }

    @Override
    public Class<? extends CurveGroupEntry> beanType() {
      return CurveGroupEntry.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code curveConfig} property.
     * @return the meta-property, not null
     */
    public MetaProperty<CurveConfig> curveConfig() {
      return curveConfig;
    }

    /**
     * The meta-property for the {@code curveKeys} property.
     * @return the meta-property, not null
     */
    public MetaProperty<ImmutableSet<MarketDataKey<YieldCurve>>> curveKeys() {
      return curveKeys;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 2042609937:  // curveConfig
          return ((CurveGroupEntry) bean).getCurveConfig();
        case 771068803:  // curveKeys
          return ((CurveGroupEntry) bean).getCurveKeys();
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
   * The bean-builder for {@code CurveGroupEntry}.
   */
  public static final class Builder extends DirectFieldsBeanBuilder<CurveGroupEntry> {

    private CurveConfig curveConfig;
    private Set<MarketDataKey<YieldCurve>> curveKeys = ImmutableSet.of();

    /**
     * Restricted constructor.
     */
    private Builder() {
    }

    /**
     * Restricted copy constructor.
     * @param beanToCopy  the bean to copy from, not null
     */
    private Builder(CurveGroupEntry beanToCopy) {
      this.curveConfig = beanToCopy.getCurveConfig();
      this.curveKeys = beanToCopy.getCurveKeys();
    }

    //-----------------------------------------------------------------------
    @Override
    public Object get(String propertyName) {
      switch (propertyName.hashCode()) {
        case 2042609937:  // curveConfig
          return curveConfig;
        case 771068803:  // curveKeys
          return curveKeys;
        default:
          throw new NoSuchElementException("Unknown property: " + propertyName);
      }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Builder set(String propertyName, Object newValue) {
      switch (propertyName.hashCode()) {
        case 2042609937:  // curveConfig
          this.curveConfig = (CurveConfig) newValue;
          break;
        case 771068803:  // curveKeys
          this.curveKeys = (Set<MarketDataKey<YieldCurve>>) newValue;
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
    public CurveGroupEntry build() {
      return new CurveGroupEntry(
          curveConfig,
          curveKeys);
    }

    //-----------------------------------------------------------------------
    /**
     * Sets the {@code curveConfig} property in the builder.
     * @param curveConfig  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder curveConfig(CurveConfig curveConfig) {
      JodaBeanUtils.notNull(curveConfig, "curveConfig");
      this.curveConfig = curveConfig;
      return this;
    }

    /**
     * Sets the {@code curveKeys} property in the builder.
     * @param curveKeys  the new value, not empty
     * @return this, for chaining, not null
     */
    public Builder curveKeys(Set<MarketDataKey<YieldCurve>> curveKeys) {
      JodaBeanUtils.notEmpty(curveKeys, "curveKeys");
      this.curveKeys = curveKeys;
      return this;
    }

    //-----------------------------------------------------------------------
    @Override
    public String toString() {
      StringBuilder buf = new StringBuilder(96);
      buf.append("CurveGroupEntry.Builder{");
      buf.append("curveConfig").append('=').append(JodaBeanUtils.toString(curveConfig)).append(',').append(' ');
      buf.append("curveKeys").append('=').append(JodaBeanUtils.toString(curveKeys));
      buf.append('}');
      return buf.toString();
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}