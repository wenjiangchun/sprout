package com.sprout.work.entity;

import com.sprout.core.jpa.entity.AbstractBaseEntity;
import com.sprout.system.entity.User;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "d_work_dairy")
public class WorkDairy extends AbstractBaseEntity<Long> {

    private static final long serialVersionUID = 1L;

    private Date workDay;

    private String weekDay;

    private Integer weekNum;

    private User worker;

    private String content;

    private String remark;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    public Date getWorkDay() {
        return workDay;
    }

    public void setWorkDay(Date workDay) {
        this.workDay = workDay;
    }

    public String getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(String weekDay) {
        this.weekDay = weekDay;
    }

    public Integer getWeekNum() {
        return weekNum;
    }

    public void setWeekNum(Integer weekNum) {
        this.weekNum = weekNum;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "worker_id")
    public User getWorker() {
        return worker;
    }

    public void setWorker(User worker) {
        this.worker = worker;
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

    @Override
    public String toString() {
        return "WorkDairy{" +
                "workDay=" + workDay +
                ", weekDay='" + weekDay + '\'' +
                ", weekNum=" + weekNum +
                ", worker=" + worker +
                ", content='" + content + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}
