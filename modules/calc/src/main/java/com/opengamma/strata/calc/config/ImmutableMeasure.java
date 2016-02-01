/**
 * Copyright (C) 2015 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.strata.calc.config;

import java.io.Serializable;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.regex.Pattern;

import org.joda.beans.Bean;
import org.joda.beans.BeanDefinition;
import org.joda.beans.ImmutableBean;
import org.joda.beans.ImmutableValidator;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.Property;
import org.joda.beans.PropertyDefinition;
import org.joda.beans.impl.direct.DirectFieldsBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaBean;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

/**
 * The default, immutable implementation of {@link Measure}.
 */
@BeanDefinition(constructorScope = "private")
public final class ImmutableMeasure
    implements Measure, ImmutableBean, Serializable {

  /**
   * Pattern for checking the name.
   * It must only contains the characters A-Z, a-z, 0-9 and -.
   */
  private static final Pattern NAME_PATTERN = Pattern.compile("[A-Za-z0-9-]+");

  /**
   * The measure name.
   * <p>
   * Measure names must only contains the characters A-Z, a-z, 0-9 and -.
   */
  @PropertyDefinition(validate = "notNull", overrideGet = true)
  private final String name;

  /**
   * Flag indicating whether measure values should be automatically converted to the reporting currency.
   */
  @PropertyDefinition(validate = "notNull", overrideGet = true)
  private final boolean currencyConvertible;

  //--------------------------------------------------------------------------------------------------

  @ImmutableValidator
  private void validate() {
    if (!NAME_PATTERN.matcher(name).matches()) {
      throw new IllegalArgumentException("Measure name must only contain the characters A-Z, a-z, 0-9 and -");
    }
  }

  /**
   * Returns a measure with the specified name whose values will be automatically converted to the reporting currency.
   * <p>
   * Measure names must only contains the characters A-Z, a-z, 0-9 and -.
   *
   * @param name  the measure name
   * @return a measure with the specified name
   */
  public static ImmutableMeasure of(String name) {
    return new ImmutableMeasure(name, true);
  }

  /**
   * Returns a measure with the specified name.
   * <p>
   * Measure names must only contains the characters A-Z, a-z, 0-9 and -.
   *
   * @param name  the measure name
   * @param isCurrencyConvertible  flag indicating whether measure values should be automatically
   * converted to the reporting currency.
   * @return a measure with the specified name
   */
  public static ImmutableMeasure of(String name, boolean isCurrencyConvertible) {
    return new ImmutableMeasure(name, isCurrencyConvertible);
  }

  @Override
  public String toString() {
    return getName();
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code ImmutableMeasure}.
   * @return the meta-bean, not null
   */
  public static ImmutableMeasure.Meta meta() {
    return ImmutableMeasure.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(ImmutableMeasure.Meta.INSTANCE);
  }

  /**
   * The serialization version id.
   */
  private static final long serialVersionUID = 1L;

  /**
   * Returns a builder used to create an instance of the bean.
   * @return the builder, not null
   */
  public static ImmutableMeasure.Builder builder() {
    return new ImmutableMeasure.Builder();
  }

  private ImmutableMeasure(
      String name,
      boolean currencyConvertible) {
    JodaBeanUtils.notNull(name, "name");
    JodaBeanUtils.notNull(currencyConvertible, "currencyConvertible");
    this.name = name;
    this.currencyConvertible = currencyConvertible;
    validate();
  }

  @Override
  public ImmutableMeasure.Meta metaBean() {
    return ImmutableMeasure.Meta.INSTANCE;
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
   * Gets the measure name.
   * <p>
   * Measure names must only contains the characters A-Z, a-z, 0-9 and -.
   * @return the value of the property, not null
   */
  @Override
  public String getName() {
    return name;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets flag indicating whether measure values should be automatically converted to the reporting currency.
   * @return the value of the property, not null
   */
  @Override
  public boolean isCurrencyConvertible() {
    return currencyConvertible;
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
      ImmutableMeasure other = (ImmutableMeasure) obj;
      return JodaBeanUtils.equal(name, other.name) &&
          (currencyConvertible == other.currencyConvertible);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = getClass().hashCode();
    hash = hash * 31 + JodaBeanUtils.hashCode(name);
    hash = hash * 31 + JodaBeanUtils.hashCode(currencyConvertible);
    return hash;
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code ImmutableMeasure}.
   */
  public static final class Meta extends DirectMetaBean {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code name} property.
     */
    private final MetaProperty<String> name = DirectMetaProperty.ofImmutable(
        this, "name", ImmutableMeasure.class, String.class);
    /**
     * The meta-property for the {@code currencyConvertible} property.
     */
    private final MetaProperty<Boolean> currencyConvertible = DirectMetaProperty.ofImmutable(
        this, "currencyConvertible", ImmutableMeasure.class, Boolean.TYPE);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> metaPropertyMap$ = new DirectMetaPropertyMap(
        this, null,
        "name",
        "currencyConvertible");

    /**
     * Restricted constructor.
     */
    private Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case 3373707:  // name
          return name;
        case 1098971060:  // currencyConvertible
          return currencyConvertible;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public ImmutableMeasure.Builder builder() {
      return new ImmutableMeasure.Builder();
    }

    @Override
    public Class<? extends ImmutableMeasure> beanType() {
      return ImmutableMeasure.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code name} property.
     * @return the meta-property, not null
     */
    public MetaProperty<String> name() {
      return name;
    }

    /**
     * The meta-property for the {@code currencyConvertible} property.
     * @return the meta-property, not null
     */
    public MetaProperty<Boolean> currencyConvertible() {
      return currencyConvertible;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 3373707:  // name
          return ((ImmutableMeasure) bean).getName();
        case 1098971060:  // currencyConvertible
          return ((ImmutableMeasure) bean).isCurrencyConvertible();
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
   * The bean-builder for {@code ImmutableMeasure}.
   */
  public static final class Builder extends DirectFieldsBeanBuilder<ImmutableMeasure> {

    private String name;
    private boolean currencyConvertible;

    /**
     * Restricted constructor.
     */
    private Builder() {
    }

    /**
     * Restricted copy constructor.
     * @param beanToCopy  the bean to copy from, not null
     */
    private Builder(ImmutableMeasure beanToCopy) {
      this.name = beanToCopy.getName();
      this.currencyConvertible = beanToCopy.isCurrencyConvertible();
    }

    //-----------------------------------------------------------------------
    @Override
    public Object get(String propertyName) {
      switch (propertyName.hashCode()) {
        case 3373707:  // name
          return name;
        case 1098971060:  // currencyConvertible
          return currencyConvertible;
        default:
          throw new NoSuchElementException("Unknown property: " + propertyName);
      }
    }

    @Override
    public Builder set(String propertyName, Object newValue) {
      switch (propertyName.hashCode()) {
        case 3373707:  // name
          this.name = (String) newValue;
          break;
        case 1098971060:  // currencyConvertible
          this.currencyConvertible = (Boolean) newValue;
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
    public ImmutableMeasure build() {
      return new ImmutableMeasure(
          name,
          currencyConvertible);
    }

    //-----------------------------------------------------------------------
    /**
     * Sets the measure name.
     * <p>
     * Measure names must only contains the characters A-Z, a-z, 0-9 and -.
     * @param name  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder name(String name) {
      JodaBeanUtils.notNull(name, "name");
      this.name = name;
      return this;
    }

    /**
     * Sets flag indicating whether measure values should be automatically converted to the reporting currency.
     * @param currencyConvertible  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder currencyConvertible(boolean currencyConvertible) {
      JodaBeanUtils.notNull(currencyConvertible, "currencyConvertible");
      this.currencyConvertible = currencyConvertible;
      return this;
    }

    //-----------------------------------------------------------------------
    @Override
    public String toString() {
      StringBuilder buf = new StringBuilder(96);
      buf.append("ImmutableMeasure.Builder{");
      buf.append("name").append('=').append(JodaBeanUtils.toString(name)).append(',').append(' ');
      buf.append("currencyConvertible").append('=').append(JodaBeanUtils.toString(currencyConvertible));
      buf.append('}');
      return buf.toString();
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}