/*
 * Copyright (C) 2022 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.strata.product.bond;

import java.io.Serializable;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Map;
import java.util.NoSuchElementException;

import org.joda.beans.Bean;
import org.joda.beans.ImmutableBean;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaBean;
import org.joda.beans.MetaProperty;
import org.joda.beans.gen.BeanDefinition;
import org.joda.beans.gen.ImmutableValidator;
import org.joda.beans.gen.PropertyDefinition;
import org.joda.beans.impl.direct.DirectFieldsBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaBean;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

import com.google.common.collect.ImmutableSet;
import com.opengamma.strata.basics.ReferenceData;
import com.opengamma.strata.basics.Resolvable;
import com.opengamma.strata.basics.currency.Currency;
import com.opengamma.strata.basics.date.AdjustableDate;
import com.opengamma.strata.collect.ArgChecker;
import com.opengamma.strata.product.Product;
import com.opengamma.strata.product.common.LongShort;

/**
 * An option on a {@link FixedCouponBond}.
 * <p>
 * The option strike is expressed as "Clean price". The "clean" price excludes any accrued interest. The clean price
 * is in the currency of the underlying bond.
 * <p>
 * The call/put is provided by the quantity's sign. If positive, it indicates the right to buy the bond (call), 
 * if negative it indicates the right to sell the bond (put).
 */
@BeanDefinition
public final class FixedCouponBondOption
    implements Product, Resolvable<ResolvedFixedCouponBondOption>, ImmutableBean, Serializable {

  /**
   * Whether the option is long or short.
   * <p>
   * Long indicates that the owner has the right to exercise the option at expiry.
   */
  @PropertyDefinition(validate = "notNull")
  private final LongShort longShort;
  /**
   * The bond underlying the option.
   */
  @PropertyDefinition(validate = "notNull")
  private final FixedCouponBond underlying;
  /**
   * The expiry date of the option.
   * <p>
   * This is the last date that the option can be exercised.
   * <p>
   * This date is typically set to be a valid business day.
   * However, the {@code businessDayAdjustment} property may be set to provide a rule for adjustment.
   */
  @PropertyDefinition(validate = "notNull")
  private final AdjustableDate expiryDate;
  /**
   * The expiry time of the option.
   * <p>
   * The expiry time is related to the expiry date and time-zone.
   */
  @PropertyDefinition(validate = "notNull")
  private final LocalTime expiryTime;
  /**
   * The time-zone of the expiry time.
   * <p>
   * The expiry time-zone is related to the expiry date and time.
   */
  @PropertyDefinition(validate = "notNull")
  private final ZoneId expiryZone;
  /**
   * The quantity that was traded.
   * <p>
   * This will be positive if buying (call) and negative if selling (put).
   */
  @PropertyDefinition
  private final double quantity;
  /**
   * The <i>clean</i> price at which the option can be exercised, in decimal form.
   * <p>
   * The "clean" price excludes any accrued interest.
   * <p>
   * Strata uses <i>decimal prices</i> for bonds in the trade model, pricers and market data.
   * For example, a price of 99.32% is represented in Strata by 0.9932.
   */
  @PropertyDefinition(validate = "ArgChecker.notNegative")
  private final double cleanStrikePrice;
  /**
   * The settlement date when the option is exercised.
   * <p>
   * This date is typically set to be a valid business day.
   * However, the {@code businessDayAdjustment} property may be set to provide a rule for adjustment.
   */
  @PropertyDefinition(validate = "notNull")
  private final AdjustableDate settlementDate;

  /**
   * The currency of the underlying bond.
   * @return the currency
   */
  public Currency getCurrency() {
    return underlying.getCurrency();
  }

  @ImmutableValidator
  private void validate() {
    ArgChecker.inOrderOrEqual(expiryDate.getUnadjusted(), settlementDate.getUnadjusted(),
        "expiry date", "settlement date");
  }

  @Override
  public ResolvedFixedCouponBondOption resolve(ReferenceData refData) {
    return ResolvedFixedCouponBondOption.builder()
        .longShort(longShort)
        .underlying(underlying.resolve(refData))
        .expiry(expiryDate.adjusted(refData).atTime(expiryTime).atZone(expiryZone))
        .quantity(quantity)
        .settlement(ResolvedFixedCouponBondSettlement.of(settlementDate.adjusted(refData), cleanStrikePrice))
        .build();
  }

  @Override
  public ImmutableSet<Currency> allCurrencies() {
    return ImmutableSet.of(getCurrency());
  }

  //------------------------- AUTOGENERATED START -------------------------
  /**
   * The meta-bean for {@code FixedCouponBondOption}.
   * @return the meta-bean, not null
   */
  public static FixedCouponBondOption.Meta meta() {
    return FixedCouponBondOption.Meta.INSTANCE;
  }

  static {
    MetaBean.register(FixedCouponBondOption.Meta.INSTANCE);
  }

  /**
   * The serialization version id.
   */
  private static final long serialVersionUID = 1L;

  /**
   * Returns a builder used to create an instance of the bean.
   * @return the builder, not null
   */
  public static FixedCouponBondOption.Builder builder() {
    return new FixedCouponBondOption.Builder();
  }

  private FixedCouponBondOption(
      LongShort longShort,
      FixedCouponBond underlying,
      AdjustableDate expiryDate,
      LocalTime expiryTime,
      ZoneId expiryZone,
      double quantity,
      double cleanStrikePrice,
      AdjustableDate settlementDate) {
    JodaBeanUtils.notNull(longShort, "longShort");
    JodaBeanUtils.notNull(underlying, "underlying");
    JodaBeanUtils.notNull(expiryDate, "expiryDate");
    JodaBeanUtils.notNull(expiryTime, "expiryTime");
    JodaBeanUtils.notNull(expiryZone, "expiryZone");
    ArgChecker.notNegative(cleanStrikePrice, "cleanStrikePrice");
    JodaBeanUtils.notNull(settlementDate, "settlementDate");
    this.longShort = longShort;
    this.underlying = underlying;
    this.expiryDate = expiryDate;
    this.expiryTime = expiryTime;
    this.expiryZone = expiryZone;
    this.quantity = quantity;
    this.cleanStrikePrice = cleanStrikePrice;
    this.settlementDate = settlementDate;
    validate();
  }

  @Override
  public FixedCouponBondOption.Meta metaBean() {
    return FixedCouponBondOption.Meta.INSTANCE;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets whether the option is long or short.
   * <p>
   * Long indicates that the owner has the right to exercise the option at expiry.
   * @return the value of the property, not null
   */
  public LongShort getLongShort() {
    return longShort;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the bond underlying the option.
   * @return the value of the property, not null
   */
  public FixedCouponBond getUnderlying() {
    return underlying;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the expiry date of the option.
   * <p>
   * This is the last date that the option can be exercised.
   * <p>
   * This date is typically set to be a valid business day.
   * However, the {@code businessDayAdjustment} property may be set to provide a rule for adjustment.
   * @return the value of the property, not null
   */
  public AdjustableDate getExpiryDate() {
    return expiryDate;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the expiry time of the option.
   * <p>
   * The expiry time is related to the expiry date and time-zone.
   * @return the value of the property, not null
   */
  public LocalTime getExpiryTime() {
    return expiryTime;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the time-zone of the expiry time.
   * <p>
   * The expiry time-zone is related to the expiry date and time.
   * @return the value of the property, not null
   */
  public ZoneId getExpiryZone() {
    return expiryZone;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the quantity that was traded.
   * <p>
   * This will be positive if buying (call) and negative if selling (put).
   * @return the value of the property
   */
  public double getQuantity() {
    return quantity;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the <i>clean</i> price at which the option can be exercised, in decimal form.
   * <p>
   * The "clean" price excludes any accrued interest.
   * <p>
   * Strata uses <i>decimal prices</i> for bonds in the trade model, pricers and market data.
   * For example, a price of 99.32% is represented in Strata by 0.9932.
   * @return the value of the property
   */
  public double getCleanStrikePrice() {
    return cleanStrikePrice;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the settlement date when the option is exercised.
   * <p>
   * This date is typically set to be a valid business day.
   * However, the {@code businessDayAdjustment} property may be set to provide a rule for adjustment.
   * @return the value of the property, not null
   */
  public AdjustableDate getSettlementDate() {
    return settlementDate;
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
      FixedCouponBondOption other = (FixedCouponBondOption) obj;
      return JodaBeanUtils.equal(longShort, other.longShort) &&
          JodaBeanUtils.equal(underlying, other.underlying) &&
          JodaBeanUtils.equal(expiryDate, other.expiryDate) &&
          JodaBeanUtils.equal(expiryTime, other.expiryTime) &&
          JodaBeanUtils.equal(expiryZone, other.expiryZone) &&
          JodaBeanUtils.equal(quantity, other.quantity) &&
          JodaBeanUtils.equal(cleanStrikePrice, other.cleanStrikePrice) &&
          JodaBeanUtils.equal(settlementDate, other.settlementDate);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = getClass().hashCode();
    hash = hash * 31 + JodaBeanUtils.hashCode(longShort);
    hash = hash * 31 + JodaBeanUtils.hashCode(underlying);
    hash = hash * 31 + JodaBeanUtils.hashCode(expiryDate);
    hash = hash * 31 + JodaBeanUtils.hashCode(expiryTime);
    hash = hash * 31 + JodaBeanUtils.hashCode(expiryZone);
    hash = hash * 31 + JodaBeanUtils.hashCode(quantity);
    hash = hash * 31 + JodaBeanUtils.hashCode(cleanStrikePrice);
    hash = hash * 31 + JodaBeanUtils.hashCode(settlementDate);
    return hash;
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(288);
    buf.append("FixedCouponBondOption{");
    buf.append("longShort").append('=').append(JodaBeanUtils.toString(longShort)).append(',').append(' ');
    buf.append("underlying").append('=').append(JodaBeanUtils.toString(underlying)).append(',').append(' ');
    buf.append("expiryDate").append('=').append(JodaBeanUtils.toString(expiryDate)).append(',').append(' ');
    buf.append("expiryTime").append('=').append(JodaBeanUtils.toString(expiryTime)).append(',').append(' ');
    buf.append("expiryZone").append('=').append(JodaBeanUtils.toString(expiryZone)).append(',').append(' ');
    buf.append("quantity").append('=').append(JodaBeanUtils.toString(quantity)).append(',').append(' ');
    buf.append("cleanStrikePrice").append('=').append(JodaBeanUtils.toString(cleanStrikePrice)).append(',').append(' ');
    buf.append("settlementDate").append('=').append(JodaBeanUtils.toString(settlementDate));
    buf.append('}');
    return buf.toString();
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code FixedCouponBondOption}.
   */
  public static final class Meta extends DirectMetaBean {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code longShort} property.
     */
    private final MetaProperty<LongShort> longShort = DirectMetaProperty.ofImmutable(
        this, "longShort", FixedCouponBondOption.class, LongShort.class);
    /**
     * The meta-property for the {@code underlying} property.
     */
    private final MetaProperty<FixedCouponBond> underlying = DirectMetaProperty.ofImmutable(
        this, "underlying", FixedCouponBondOption.class, FixedCouponBond.class);
    /**
     * The meta-property for the {@code expiryDate} property.
     */
    private final MetaProperty<AdjustableDate> expiryDate = DirectMetaProperty.ofImmutable(
        this, "expiryDate", FixedCouponBondOption.class, AdjustableDate.class);
    /**
     * The meta-property for the {@code expiryTime} property.
     */
    private final MetaProperty<LocalTime> expiryTime = DirectMetaProperty.ofImmutable(
        this, "expiryTime", FixedCouponBondOption.class, LocalTime.class);
    /**
     * The meta-property for the {@code expiryZone} property.
     */
    private final MetaProperty<ZoneId> expiryZone = DirectMetaProperty.ofImmutable(
        this, "expiryZone", FixedCouponBondOption.class, ZoneId.class);
    /**
     * The meta-property for the {@code quantity} property.
     */
    private final MetaProperty<Double> quantity = DirectMetaProperty.ofImmutable(
        this, "quantity", FixedCouponBondOption.class, Double.TYPE);
    /**
     * The meta-property for the {@code cleanStrikePrice} property.
     */
    private final MetaProperty<Double> cleanStrikePrice = DirectMetaProperty.ofImmutable(
        this, "cleanStrikePrice", FixedCouponBondOption.class, Double.TYPE);
    /**
     * The meta-property for the {@code settlementDate} property.
     */
    private final MetaProperty<AdjustableDate> settlementDate = DirectMetaProperty.ofImmutable(
        this, "settlementDate", FixedCouponBondOption.class, AdjustableDate.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> metaPropertyMap$ = new DirectMetaPropertyMap(
        this, null,
        "longShort",
        "underlying",
        "expiryDate",
        "expiryTime",
        "expiryZone",
        "quantity",
        "cleanStrikePrice",
        "settlementDate");

    /**
     * Restricted constructor.
     */
    private Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case 116685664:  // longShort
          return longShort;
        case -1770633379:  // underlying
          return underlying;
        case -816738431:  // expiryDate
          return expiryDate;
        case -816254304:  // expiryTime
          return expiryTime;
        case -816069761:  // expiryZone
          return expiryZone;
        case -1285004149:  // quantity
          return quantity;
        case 1424472750:  // cleanStrikePrice
          return cleanStrikePrice;
        case -295948169:  // settlementDate
          return settlementDate;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public FixedCouponBondOption.Builder builder() {
      return new FixedCouponBondOption.Builder();
    }

    @Override
    public Class<? extends FixedCouponBondOption> beanType() {
      return FixedCouponBondOption.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code longShort} property.
     * @return the meta-property, not null
     */
    public MetaProperty<LongShort> longShort() {
      return longShort;
    }

    /**
     * The meta-property for the {@code underlying} property.
     * @return the meta-property, not null
     */
    public MetaProperty<FixedCouponBond> underlying() {
      return underlying;
    }

    /**
     * The meta-property for the {@code expiryDate} property.
     * @return the meta-property, not null
     */
    public MetaProperty<AdjustableDate> expiryDate() {
      return expiryDate;
    }

    /**
     * The meta-property for the {@code expiryTime} property.
     * @return the meta-property, not null
     */
    public MetaProperty<LocalTime> expiryTime() {
      return expiryTime;
    }

    /**
     * The meta-property for the {@code expiryZone} property.
     * @return the meta-property, not null
     */
    public MetaProperty<ZoneId> expiryZone() {
      return expiryZone;
    }

    /**
     * The meta-property for the {@code quantity} property.
     * @return the meta-property, not null
     */
    public MetaProperty<Double> quantity() {
      return quantity;
    }

    /**
     * The meta-property for the {@code cleanStrikePrice} property.
     * @return the meta-property, not null
     */
    public MetaProperty<Double> cleanStrikePrice() {
      return cleanStrikePrice;
    }

    /**
     * The meta-property for the {@code settlementDate} property.
     * @return the meta-property, not null
     */
    public MetaProperty<AdjustableDate> settlementDate() {
      return settlementDate;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 116685664:  // longShort
          return ((FixedCouponBondOption) bean).getLongShort();
        case -1770633379:  // underlying
          return ((FixedCouponBondOption) bean).getUnderlying();
        case -816738431:  // expiryDate
          return ((FixedCouponBondOption) bean).getExpiryDate();
        case -816254304:  // expiryTime
          return ((FixedCouponBondOption) bean).getExpiryTime();
        case -816069761:  // expiryZone
          return ((FixedCouponBondOption) bean).getExpiryZone();
        case -1285004149:  // quantity
          return ((FixedCouponBondOption) bean).getQuantity();
        case 1424472750:  // cleanStrikePrice
          return ((FixedCouponBondOption) bean).getCleanStrikePrice();
        case -295948169:  // settlementDate
          return ((FixedCouponBondOption) bean).getSettlementDate();
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
   * The bean-builder for {@code FixedCouponBondOption}.
   */
  public static final class Builder extends DirectFieldsBeanBuilder<FixedCouponBondOption> {

    private LongShort longShort;
    private FixedCouponBond underlying;
    private AdjustableDate expiryDate;
    private LocalTime expiryTime;
    private ZoneId expiryZone;
    private double quantity;
    private double cleanStrikePrice;
    private AdjustableDate settlementDate;

    /**
     * Restricted constructor.
     */
    private Builder() {
    }

    /**
     * Restricted copy constructor.
     * @param beanToCopy  the bean to copy from, not null
     */
    private Builder(FixedCouponBondOption beanToCopy) {
      this.longShort = beanToCopy.getLongShort();
      this.underlying = beanToCopy.getUnderlying();
      this.expiryDate = beanToCopy.getExpiryDate();
      this.expiryTime = beanToCopy.getExpiryTime();
      this.expiryZone = beanToCopy.getExpiryZone();
      this.quantity = beanToCopy.getQuantity();
      this.cleanStrikePrice = beanToCopy.getCleanStrikePrice();
      this.settlementDate = beanToCopy.getSettlementDate();
    }

    //-----------------------------------------------------------------------
    @Override
    public Object get(String propertyName) {
      switch (propertyName.hashCode()) {
        case 116685664:  // longShort
          return longShort;
        case -1770633379:  // underlying
          return underlying;
        case -816738431:  // expiryDate
          return expiryDate;
        case -816254304:  // expiryTime
          return expiryTime;
        case -816069761:  // expiryZone
          return expiryZone;
        case -1285004149:  // quantity
          return quantity;
        case 1424472750:  // cleanStrikePrice
          return cleanStrikePrice;
        case -295948169:  // settlementDate
          return settlementDate;
        default:
          throw new NoSuchElementException("Unknown property: " + propertyName);
      }
    }

    @Override
    public Builder set(String propertyName, Object newValue) {
      switch (propertyName.hashCode()) {
        case 116685664:  // longShort
          this.longShort = (LongShort) newValue;
          break;
        case -1770633379:  // underlying
          this.underlying = (FixedCouponBond) newValue;
          break;
        case -816738431:  // expiryDate
          this.expiryDate = (AdjustableDate) newValue;
          break;
        case -816254304:  // expiryTime
          this.expiryTime = (LocalTime) newValue;
          break;
        case -816069761:  // expiryZone
          this.expiryZone = (ZoneId) newValue;
          break;
        case -1285004149:  // quantity
          this.quantity = (Double) newValue;
          break;
        case 1424472750:  // cleanStrikePrice
          this.cleanStrikePrice = (Double) newValue;
          break;
        case -295948169:  // settlementDate
          this.settlementDate = (AdjustableDate) newValue;
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
    public FixedCouponBondOption build() {
      return new FixedCouponBondOption(
          longShort,
          underlying,
          expiryDate,
          expiryTime,
          expiryZone,
          quantity,
          cleanStrikePrice,
          settlementDate);
    }

    //-----------------------------------------------------------------------
    /**
     * Sets whether the option is long or short.
     * <p>
     * Long indicates that the owner has the right to exercise the option at expiry.
     * @param longShort  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder longShort(LongShort longShort) {
      JodaBeanUtils.notNull(longShort, "longShort");
      this.longShort = longShort;
      return this;
    }

    /**
     * Sets the bond underlying the option.
     * @param underlying  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder underlying(FixedCouponBond underlying) {
      JodaBeanUtils.notNull(underlying, "underlying");
      this.underlying = underlying;
      return this;
    }

    /**
     * Sets the expiry date of the option.
     * <p>
     * This is the last date that the option can be exercised.
     * <p>
     * This date is typically set to be a valid business day.
     * However, the {@code businessDayAdjustment} property may be set to provide a rule for adjustment.
     * @param expiryDate  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder expiryDate(AdjustableDate expiryDate) {
      JodaBeanUtils.notNull(expiryDate, "expiryDate");
      this.expiryDate = expiryDate;
      return this;
    }

    /**
     * Sets the expiry time of the option.
     * <p>
     * The expiry time is related to the expiry date and time-zone.
     * @param expiryTime  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder expiryTime(LocalTime expiryTime) {
      JodaBeanUtils.notNull(expiryTime, "expiryTime");
      this.expiryTime = expiryTime;
      return this;
    }

    /**
     * Sets the time-zone of the expiry time.
     * <p>
     * The expiry time-zone is related to the expiry date and time.
     * @param expiryZone  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder expiryZone(ZoneId expiryZone) {
      JodaBeanUtils.notNull(expiryZone, "expiryZone");
      this.expiryZone = expiryZone;
      return this;
    }

    /**
     * Sets the quantity that was traded.
     * <p>
     * This will be positive if buying (call) and negative if selling (put).
     * @param quantity  the new value
     * @return this, for chaining, not null
     */
    public Builder quantity(double quantity) {
      this.quantity = quantity;
      return this;
    }

    /**
     * Sets the <i>clean</i> price at which the option can be exercised, in decimal form.
     * <p>
     * The "clean" price excludes any accrued interest.
     * <p>
     * Strata uses <i>decimal prices</i> for bonds in the trade model, pricers and market data.
     * For example, a price of 99.32% is represented in Strata by 0.9932.
     * @param cleanStrikePrice  the new value
     * @return this, for chaining, not null
     */
    public Builder cleanStrikePrice(double cleanStrikePrice) {
      ArgChecker.notNegative(cleanStrikePrice, "cleanStrikePrice");
      this.cleanStrikePrice = cleanStrikePrice;
      return this;
    }

    /**
     * Sets the settlement date when the option is exercised.
     * <p>
     * This date is typically set to be a valid business day.
     * However, the {@code businessDayAdjustment} property may be set to provide a rule for adjustment.
     * @param settlementDate  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder settlementDate(AdjustableDate settlementDate) {
      JodaBeanUtils.notNull(settlementDate, "settlementDate");
      this.settlementDate = settlementDate;
      return this;
    }

    //-----------------------------------------------------------------------
    @Override
    public String toString() {
      StringBuilder buf = new StringBuilder(288);
      buf.append("FixedCouponBondOption.Builder{");
      buf.append("longShort").append('=').append(JodaBeanUtils.toString(longShort)).append(',').append(' ');
      buf.append("underlying").append('=').append(JodaBeanUtils.toString(underlying)).append(',').append(' ');
      buf.append("expiryDate").append('=').append(JodaBeanUtils.toString(expiryDate)).append(',').append(' ');
      buf.append("expiryTime").append('=').append(JodaBeanUtils.toString(expiryTime)).append(',').append(' ');
      buf.append("expiryZone").append('=').append(JodaBeanUtils.toString(expiryZone)).append(',').append(' ');
      buf.append("quantity").append('=').append(JodaBeanUtils.toString(quantity)).append(',').append(' ');
      buf.append("cleanStrikePrice").append('=').append(JodaBeanUtils.toString(cleanStrikePrice)).append(',').append(' ');
      buf.append("settlementDate").append('=').append(JodaBeanUtils.toString(settlementDate));
      buf.append('}');
      return buf.toString();
    }

  }

  //-------------------------- AUTOGENERATED END --------------------------
}