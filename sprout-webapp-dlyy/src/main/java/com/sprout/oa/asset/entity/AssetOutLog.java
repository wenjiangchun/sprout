package com.sprout.oa.asset.entity;

import com.sprout.core.jpa.entity.AbstractBaseCommonEntity;
import com.sprout.core.jpa.entity.AbstractBaseEntity;
import com.sprout.oa.leave.entity.Leave;
import com.sprout.system.entity.User;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "wk_asset_out_log")
public class AssetOutLog extends AbstractBaseEntity<Long> {

    private AssetOut assetOut;

    private User handler;

    private String result;

    private String taskName;

    private Date handleTime = new Date();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "asset_out_id")
    public AssetOut getAssetOut() {
        return assetOut;
    }

    public void setAssetOut(AssetOut assetOut) {
        this.assetOut = assetOut;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "handler_id")
    public User getHandler() {
        return handler;
    }

    public void setHandler(User handler) {
        this.handler = handler;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    public Date getHandleTime() {
        return handleTime;
    }

    public void setHandleTime(Date handleTime) {
        this.handleTime = handleTime;
    }

    public String getResult() {
        return result;
    }

    @Override
    public String toString() {
        return "AssetOutLog{" +
                "id=" + id +
                ", assetOut=" + assetOut +
                ", handler=" + handler +
                ", result='" + result + '\'' +
                ", taskName='" + taskName + '\'' +
                ", handleTime=" + handleTime +
                '}';
    }
}
