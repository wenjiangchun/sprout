package com.sprout.oa.asset.entity;

import com.sprout.core.jpa.entity.AbstractBaseCommonEntity;
import com.sprout.system.entity.User;

import javax.persistence.*;

@Entity
@Table(name = "wk_asset_out")
public class AssetOut extends AbstractBaseCommonEntity<Long>  {

    /**
     * 对应流程KEY
     */
    private static final String PROCESS_KEY = "assetOutProcess";

    private Asset asset;

    private User applier;

    private User operator;

    private int count;

    private int result;

    private String processInstanceId;

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "applier_id")
    public User getApplier() {
        return applier;
    }

    public void setApplier(User applier) {
        this.applier = applier;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

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
                ", processInstanceId=" + processInstanceId +
                '}';
    }
}
