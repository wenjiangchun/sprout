package com.sprout.work.entity;

import com.sprout.core.jpa.entity.AbstractBaseEntity;
import com.sprout.system.entity.User;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "d_dairy_send_config")
public class DairySendLog extends AbstractBaseEntity<Long> {

    private static final long serialVersionUID = 1L;

    private User user;

    private Date sendTime;

    private Boolean isSend = false;

    private String errorMessage;

    public Boolean getIsSend() {
        return isSend;
    }

    public void setIsSend(Boolean isSend) {
        this.isSend = isSend;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "worker_id")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    @Override
    public String toString() {
        return "DairySendLog{" +
                "id=" + id +
                ", user=" + user +
                ", sendTime=" + sendTime +
                ", isSend=" + isSend +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
