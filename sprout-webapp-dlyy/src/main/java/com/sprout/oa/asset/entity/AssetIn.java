package com.sprout.oa.asset.entity;

import com.sprout.core.jpa.entity.AbstractBaseCommonEntity;
import com.sprout.system.entity.Dict;
import com.sprout.system.entity.User;

import javax.persistence.*;

@Entity
@Table(name = "wk_asset_in")
public class AssetIn extends AbstractBaseCommonEntity<Long>  {

    private Asset asset;

    private User operator;

    private int count;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "asset_id")
    public Asset getAsset() {
        return asset;
    }

    public void setAsset(Asset asset) {
        this.asset = asset;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "operator_id")
    public User getOperator() {
        return operator;
    }

    public void setOperator(User operator) {
        this.operator = operator;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "AssetIn{" +
                "createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", id=" + id +
                ", asset=" + asset +
                ", operator=" + operator +
                ", count=" + count +
                '}';
    }
}
