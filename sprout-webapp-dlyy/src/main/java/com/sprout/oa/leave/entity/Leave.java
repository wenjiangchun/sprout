package com.sprout.oa.leave.entity;

import com.sprout.core.jpa.entity.AbstractBaseEntity;
import com.sprout.system.entity.Dict;
import com.sprout.system.entity.User;
import com.sprout.work.entity.HolidayItem;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "w_leave")
public class Leave extends AbstractBaseEntity<Long> {

    private static final long serialVersionUID = 1L;

    private User applier;

    private Dict leaveType;

    private Date applyTime;

    private Date planStartTime;

    private Date planEndTime;

    private String content;

    private String remark;

    private String processInstanceId;

    private Date realStartTime;

    private Date realEndTime;

    private Date backTime;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "applier_id")
    public User getApplier() {
        return applier;
    }

    public void setApplier(User applier) {
        this.applier = applier;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "leaveType_id")
    public Dict getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(Dict leaveType) {
        this.leaveType = leaveType;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    public Date getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
    }

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    public Date getPlanStartTime() {
        return planStartTime;
    }

    public void setPlanStartTime(Date planStartTime) {
        this.planStartTime = planStartTime;
    }

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    public Date getPlanEndTime() {
        return planEndTime;
    }

    public void setPlanEndTime(Date planEndTime) {
        this.planEndTime = planEndTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    public Date getRealStartTime() {
        return realStartTime;
    }

    public void setRealStartTime(Date realStartTime) {
        this.realStartTime = realStartTime;
    }

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    public Date getRealEndTime() {
        return realEndTime;
    }

    public void setRealEndTime(Date realEndTime) {
        this.realEndTime = realEndTime;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    public Date getBackTime() {
        return backTime;
    }

    public void setBackTime(Date backTime) {
        this.backTime = backTime;
    }

    @Override
    public String toString() {
        return "Leave{" +
                "id=" + id +
                ", applier=" + applier +
                ", leaveType=" + leaveType +
                ", applyTime=" + applyTime +
                ", planStartTime=" + planStartTime +
                ", planEndTime=" + planEndTime +
                ", content='" + content + '\'' +
                ", remark='" + remark + '\'' +
                ", processInstanceId='" + processInstanceId + '\'' +
                ", realStartTime=" + realStartTime +
                ", realEndTime=" + realEndTime +
                ", backTime=" + backTime +
                '}';
    }
}
