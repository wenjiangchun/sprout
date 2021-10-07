package com.sprout.oa.leave.entity;

import com.sprout.core.jpa.entity.AbstractBaseEntity;
import com.sprout.system.entity.User;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "w_leave_log")
public class LeaveTaskLog extends AbstractBaseEntity<Long> {

    private static final long serialVersionUID = 1L;

    private Leave leave;

    private User handler;

    private String result;

    private Date handleTime = new Date();

    private boolean currentTask = false;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "leave_id")
    public Leave getLeave() {
        return leave;
    }

    public void setLeave(Leave leave) {
        this.leave = leave;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "handler_id")
    public User getHandler() {
        return handler;
    }

    public void setHandler(User handler) {
        this.handler = handler;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    public Date getHandleTime() {
        return handleTime;
    }

    public void setHandleTime(Date handleTime) {
        this.handleTime = handleTime;
    }

    public boolean isCurrentTask() {
        return currentTask;
    }

    public void setCurrentTask(boolean currentTask) {
        this.currentTask = currentTask;
    }

    @Override
    public String toString() {
        return "LeaveTaskLog{" +
                "id=" + id +
                ", leave=" + leave +
                ", handler=" + handler +
                ", result='" + result + '\'' +
                ", handleTime=" + handleTime +
                ", currentTask=" + currentTask +
                '}';
    }
}
