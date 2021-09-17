package com.sprout.dlyy.monitor.entity;

import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

/**
 * 打卡记录封装类
 */
public class MonitorRecord implements Serializable {


    private String recordDate;

    private String name;

    private String amState;

    private String pmState;

    @Field("time_today")
    public String getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(String recordDate) {
        this.recordDate = recordDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Field("am_state")
    public String getAmState() {
        return amState;
    }

    public void setAmState(String amState) {
        this.amState = amState;
    }

    @Field("pm_state")
    public String getPmState() {
        return pmState;
    }

    public void setPmState(String pmState) {
        this.pmState = pmState;
    }
}
