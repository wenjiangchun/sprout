package com.sprout.dlyy.monitor.entity;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.io.Serializable;

/**
 * 打卡规则配置实体类
 */
@Document(collection = "attendance_config")
public class AttendanceConfig implements Serializable {

    @MongoId
    private ObjectId id;

    @Field(name="buffer_ Worktime")
    private String bufferWorktime;

    @Field(name="late_absence_time")
    private String lateAbsenceTime;

    @Field(name="leave_absence_time")
    private String leaveAbsenceTime;

    @Field(name="sw_start_time")
    private String swStartTime;

    @Field(name="sw_end_time")
    private String swEndTime;

    @Field(name="xw_start_time")
    private String xwStartTime;

    @Field(name="xw_end_time")
    private String xwEndTime;

    @Field(name="sw_judge_start_time")
    private String swJudgeStartTime;

    @Field(name="sw_judge_end_time")
    private String swJudgeEndTime;

    @Field(name="xw_judge_start_time")
    private String xwJudgeStartTime;

    @Field(name="xw_judge_end_time")
    private String xwJudgeEndTime;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getBufferWorktime() {
        return bufferWorktime;
    }

    public void setBufferWorktime(String bufferWorktime) {
        this.bufferWorktime = bufferWorktime;
    }

    public String getLateAbsenceTime() {
        return lateAbsenceTime;
    }

    public void setLateAbsenceTime(String lateAbsenceTime) {
        this.lateAbsenceTime = lateAbsenceTime;
    }

    public String getLeaveAbsenceTime() {
        return leaveAbsenceTime;
    }

    public void setLeaveAbsenceTime(String leaveAbsenceTime) {
        this.leaveAbsenceTime = leaveAbsenceTime;
    }

    public String getSwStartTime() {
        return swStartTime;
    }

    public void setSwStartTime(String swStartTime) {
        this.swStartTime = swStartTime;
    }

    public String getSwEndTime() {
        return swEndTime;
    }

    public void setSwEndTime(String swEndTime) {
        this.swEndTime = swEndTime;
    }

    public String getXwStartTime() {
        return xwStartTime;
    }

    public void setXwStartTime(String xwStartTime) {
        this.xwStartTime = xwStartTime;
    }

    public String getXwEndTime() {
        return xwEndTime;
    }

    public void setXwEndTime(String xwEndTime) {
        this.xwEndTime = xwEndTime;
    }

    public String getSwJudgeStartTime() {
        return swJudgeStartTime;
    }

    public void setSwJudgeStartTime(String swJudgeStartTime) {
        this.swJudgeStartTime = swJudgeStartTime;
    }

    public String getSwJudgeEndTime() {
        return swJudgeEndTime;
    }

    public void setSwJudgeEndTime(String swJudgeEndTime) {
        this.swJudgeEndTime = swJudgeEndTime;
    }

    public String getXwJudgeStartTime() {
        return xwJudgeStartTime;
    }

    public void setXwJudgeStartTime(String xwJudgeStartTime) {
        this.xwJudgeStartTime = xwJudgeStartTime;
    }

    public String getXwJudgeEndTime() {
        return xwJudgeEndTime;
    }

    public void setXwJudgeEndTime(String xwJudgeEndTime) {
        this.xwJudgeEndTime = xwJudgeEndTime;
    }

    @Override
    public String toString() {
        return "AttendanceConfig{" +
                "id='" + id + '\'' +
                ", bufferWorktime='" + bufferWorktime + '\'' +
                ", lateAbsenceTime='" + lateAbsenceTime + '\'' +
                ", leaveAbsenceTime='" + leaveAbsenceTime + '\'' +
                ", swStartTime='" + swStartTime + '\'' +
                ", swEndTime='" + swEndTime + '\'' +
                ", xwStartTime='" + xwStartTime + '\'' +
                ", xwEndTime='" + xwEndTime + '\'' +
                ", swJudgeStartTime='" + swJudgeStartTime + '\'' +
                ", swJudgeEndTime='" + swJudgeEndTime + '\'' +
                ", xwJudgeStartTime='" + xwJudgeStartTime + '\'' +
                ", xwJudgeEndTime='" + xwJudgeEndTime + '\'' +
                '}';
    }
}
