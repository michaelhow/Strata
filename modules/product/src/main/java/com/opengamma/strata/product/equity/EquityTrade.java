/**
 * Copyright (C) 2015 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.strata.product.equity;

import java.io.Serializable;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import org.joda.beans.Bean;
import org.joda.beans.BeanDefinition;
import org.joda.beans.ImmutableBean;
import org.joda.beans.ImmutableDefaults;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.Property;
import org.joda.beans.PropertyDefinition;
import org.joda.beans.impl.direct.DirectFieldsBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaBean;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

import com.opengamma.strata.product.SecuritizedProductTrade;
import com.opengamma.strata.product.TradeInfo;

/**
 * A trade representing the purchase or sale of an equity.
 * <p>
 * A trade in an underlying {@link Equity}.
 * For example, the purchase of 1000 shares of OpenGamma.
 */
@BeanDefinition(constructorScope = "package")
public final class EquityTrade
    implements SecuritizedProductTrade, ImmutableBean, Serializable {

  /**
   * The additional trade information, defaulted to an empty instance.
   * <p>
   * This allows additional information to be attached to the trade.
   */
  @PropertyDefinition(overrideGet = true)
  private final TradeInfo info;
  /**
   * The equity that was traded.
   * <p>
   * The product captures the contracted financial details of the trade.
   */
  @PropertyDefinition(validate = "notNull", overrideGet = true)
  private final Equity product;
  /**
   * The quantity that was traded.
   * <p>
   * This will be positive if buying and negative if selling.
   */
  @PropertyDefinition(overrideGet = true)
  private final double quantity;
  /**
   * The price that was traded.
   * <p>
   * This is the price agreed when the trade occurred.
   */
  @PropertyDefinition(overrideGet = true)
  private final double price;

  //-------------------------------------------------------------------------
  @ImmutableDefaults
  private static void applyDefaults(Builder builder) {
    builder.info = TradeInfo.empty();
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code EquityTrade}.
   * @return the meta-bean, not null
   */
  public static EquityTrade.Meta meta() {
    return EquityTrade.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(EquityTrade.Meta.INSTANCE);
  }

  /**
   * The serialization version id.
   */
  private static final long serialVersionUID = 1L;

  /**
   * Returns a builder used to create an instance of the bean.
   * @return the builder, not null
   */
  public static EquityTrade.Builder builder() {
    return new EquityTrade.Builder();
  }

  /**
   * Creates an instance.
   * @param info  the value of the property
   * @param product  the value of the property, not null
   * @param quantity  the value of the property
   * @param price  the value of the property
   */
  EquityTrade(
      TradeInfo info,
      Equity product,
      double quantity,
      double price) {
    JodaBeanUtils.notNull(product, "product");
    this.info = info;
    this.product = product;
    this.quantity = quantity;
    this.price = price;
  }

  @Override
  public EquityTrade.Meta metaBean() {
    return EquityTrade.Meta.INSTANCE;
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
   * Gets the additional trade information, defaulted to an empty instance.
   * <p>
   * This allows additional information to be attached to the trade.
   * @return the value of the property
   */
  @Override
  public TradeInfo getInfo() {
    return info;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the equity that was traded.
   * <p>
   * The product captures the contracted financial details of the trade.
   * @return the value of the property, not null
   */
  @Override
  public Equity getProduct() {
    return product;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the quantity that was traded.
   * <p>
   * This will be positive if buying and negative if selling.
   * @return the value of the property
   */
  @Override
  public double getQuantity() {
    return quantity;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the price that was traded.
   * <p>
   * This is the price agreed when the trade occurred.
   * @return the value of the property
   */
  @Override
  public double getPrice() {
    return price;
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
      EquityTrade other = (EquityTrade) obj;
      return JodaBeanUtils.equal(info, other.info) &&
          JodaBeanUtils.equal(product, other.product) &&
          JodaBeanUtils.equal(quantity, other.quantity) &&
          JodaBeanUtils.equal(price, other.price);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = getClass().hashCode();
    hash = hash * 31 + JodaBeanUtils.hashCode(info);
    hash = hash * 31 + JodaBeanUtils.hashCode(product);
    hash = hash * 31 + JodaBeanUtils.hashCode(quantity);
    hash = hash * 31 + JodaBeanUtils.hashCode(price);
    return hash;
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(160);
    buf.append("EquityTrade{");
    buf.append("info").append('=').append(info).append(',').append(' ');
    buf.append("product").append('=').append(product).append(',').append(' ');
    buf.append("quantity").append('=').append(quantity).append(',').append(' ');
    buf.append("price").append('=').append(JodaBeanUtils.toString(price));
    buf.append('}');
    return buf.toString();
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code EquityTrade}.
   */
  public static final class Meta extends DirectMetaBean {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code info} property.
     */
    private final MetaProperty<TradeInfo> info = DirectMetaProperty.ofImmutable(
        this, "info", EquityTrade.class, TradeInfo.class);
    /**
     * The meta-property for the {@code product} property.
     */
    private final MetaProperty<Equity> product = DirectMetaProperty.ofImmutable(
        this, "product", EquityTrade.class, Equity.class);
    /**
     * The meta-property for the {@code quantity} property.
     */
    private final MetaProperty<Double> quantity = DirectMetaProperty.ofImmutable(
        this, "quantity", EquityTrade.class, Double.TYPE);
    /**
     * The meta-property for the {@code price} property.
     */
    private final MetaProperty<Double> price = DirectMetaProperty.ofImmutable(
        this, "price", EquityTrade.class, Double.TYPE);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> metaPropertyMap$ = new DirectMetaPropertyMap(
        this, null,
        "info",
        "product",
        "quantity",
        "price");

    /**
     * Restricted constructor.
     */
    private Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case 3237038:  // info
          return info;
        case -309474065:  // product
          return product;
        case -1285004149:  // quantity
          return quantity;
        case 106934601:  // price
          return price;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public EquityTrade.Builder builder() {
      return new EquityTrade.Builder();
    }

    @Override
    public Class<? extends EquityTrade> beanType() {
      return EquityTrade.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code info} property.
     * @return the meta-property, not null
     */
    public MetaProperty<TradeInfo> info() {
      return info;
    }

    /**
     * The meta-property for the {@code product} property.
     * @return the meta-property, not null
     */
    public MetaProperty<Equity> product() {
      return product;
    }

    /**
     * The meta-property for the {@code quantity} property.
     * @return the meta-property, not null
     */
    public MetaProperty<Double> quantity() {
      return quantity;
    }

    /**
     * The meta-property for the {@code price} property.
     * @return the meta-property, not null
     */
    public MetaProperty<Double> price() {
      return price;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 3237038:  // info
          return ((EquityTrade) bean).getInfo();
        case -309474065:  // product
          return ((EquityTrade) bean).getProduct();
        case -1285004149:  // quantity
          return ((EquityTrade) bean).getQuantity();
        case 106934601:  // price
          return ((EquityTrade) bean).getPrice();
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
   * The bean-builder for {@code EquityTrade}.
   */
  public static final class Builder extends DirectFieldsBeanBuilder<EquityTrade> {

    private TradeInfo info;
    private Equity product;
    private double quantity;
    private double price;

    /**
     * Restricted constructor.
     */
    private Builder() {
      applyDefaults(this);
    }

    /**
     * Restricted copy constructor.
     * @param beanToCopy  the bean to copy from, not null
     */
    private Builder(EquityTrade beanToCopy) {
      this.info = beanToCopy.getInfo();
      this.product = beanToCopy.getProduct();
      this.quantity = beanToCopy.getQuantity();
      this.price = beanToCopy.getPrice();
    }

    //-----------------------------------------------------------------------
    @Override
    public Object get(String propertyName) {
      switch (propertyName.hashCode()) {
        case 3237038:  // info
          return info;
        case -309474065:  // product
          return product;
        case -1285004149:  // quantity
          return quantity;
        case 106934601:  // price
          return price;
        default:
          throw new NoSuchElementException("Unknown property: " + propertyName);
      }
    }

    @Override
    public Builder set(String propertyName, Object newValue) {
      switch (propertyName.hashCode()) {
        case 3237038:  // info
          this.info = (TradeInfo) newValue;
          break;
        case -309474065:  // product
          this.product = (Equity) newValue;
          break;
        case -1285004149:  // quantity
          this.quantity = (Double) newValue;
          break;
        case 106934601:  // price
          this.price = (Double) newValue;
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
    public EquityTrade build() {
      return new EquityTrade(
          info,
          product,
          quantity,
          price);
    }

    //-----------------------------------------------------------------------
    /**
     * Sets the additional trade information, defaulted to an empty instance.
     * <p>
     * This allows additional information to be attached to the trade.
     * @param info  the new value
     * @return this, for chaining, not null
     */
    public Builder info(TradeInfo info) {
      this.info = info;
      return this;
    }

    /**
     * Sets the equity that was traded.
     * <p>
     * The product captures the contracted financial details of the trade.
     * @param product  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder product(Equity product) {
      JodaBeanUtils.notNull(product, "product");
      this.product = product;
      return this;
    }

    /**
     * Sets the quantity that was traded.
     * <p>
     * This will be positive if buying and negative if selling.
     * @param quantity  the new value
     * @return this, for chaining, not null
     */
    public Builder quantity(double quantity) {
      this.quantity = quantity;
      return this;
    }

    /**
     * Sets the price that was traded.
     * <p>
     * This is the price agreed when the trade occurred.
     * @param price  the new value
     * @return this, for chaining, not null
     */
    public Builder price(double price) {
      this.price = price;
      return this;
    }

    //-----------------------------------------------------------------------
    @Override
    public String toString() {
      StringBuilder buf = new StringBuilder(160);
      buf.append("EquityTrade.Builder{");
      buf.append("info").append('=').append(JodaBeanUtils.toString(info)).append(',').append(' ');
      buf.append("product").append('=').append(JodaBeanUtils.toString(product)).append(',').append(' ');
      buf.append("quantity").append('=').append(JodaBeanUtils.toString(quantity)).append(',').append(' ');
      buf.append("price").append('=').append(JodaBeanUtils.toString(price));
      buf.append('}');
      return buf.toString();
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
