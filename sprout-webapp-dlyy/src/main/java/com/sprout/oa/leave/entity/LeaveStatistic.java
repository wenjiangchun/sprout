package com.sprout.oa.leave.entity;

import com.sprout.core.jpa.entity.AbstractBaseEntity;
import com.sprout.system.entity.Dict;
import com.sprout.system.entity.User;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.Map;

@Entity
@Table(name = "wk_leave_statistic")
public class LeaveStatistic extends AbstractBaseEntity<Long> {

    private static final long serialVersionUID = 1L;

    private User applier;

    private Dict leaveType;

    private int year;

    private int month;

    private float days;

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

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public float getDays() {
        return days;
    }

    public void setDays(float days) {
        this.days = days;
    }

    @Override
    public String toString() {
        return "LeaveStatis{" +
                "id=" + id +
                ", applier=" + applier +
                ", leaveType=" + leaveType +
                ", year=" + year +
                ", days=" + days +
                '}';
    }
}
