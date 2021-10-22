package com.sprout.oa.asset.entity;

import com.sprout.core.jpa.entity.AbstractBaseCommonEntity;
import com.sprout.system.entity.Dict;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "wk_asset")
public class Asset extends AbstractBaseCommonEntity<Long>  {

    public static final String DICT_BRAND = "ASSET_BRAND";

    public static final String DICT_CLASSIFY = "ASSET_CLASSIFY";

    public static final String DICT_MODEL = "ASSET_MODEL";

    public static final String DICT_UNIT = "ASSET_UNIT";

    private String assetNum;

    private Dict brand;

    private Dict classify;

    private Dict model;

    private Dict unit;

    private int type;

    private int count;

    public String getAssetNum() {
        return assetNum;
    }

    public void setAssetNum(String assetNum) {
        this.assetNum = assetNum;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "brand_id")
    public Dict getBrand() {
        return brand;
    }

    public void setBrand(Dict brand) {
        this.brand = brand;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "classify_id")
    public Dict getClassify() {
        return classify;
    }

    public void setClassify(Dict classify) {
        this.classify = classify;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "model_id")
    public Dict getModel() {
        return model;
    }

    public void setModel(Dict model) {
        this.model = model;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "unit_id")
    public Dict getUnit() {
        return unit;
    }

    public void setUnit(Dict unit) {
        this.unit = unit;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "Asset{" +
                "createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", id=" + id +
                ", assetNum='" + assetNum + '\'' +
                ", brand=" + brand +
                ", classify=" + classify +
                ", model=" + model +
                ", unit=" + unit +
                ", type=" + type +
                ", count=" + count +
                '}';
    }

    private BigDecimal price;

    private BigDecimal totalPrice;

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}
