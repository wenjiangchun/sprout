package com.sprout.work.entity;

import com.sprout.core.jpa.entity.AbstractBaseEntity;
import com.sprout.system.entity.User;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "w_dairy_send_log")
public class DairySendLog extends AbstractBaseEntity<Long> {

    private static final long serialVersionUID = 1L;

    private User user;

    private Date sendTime;

    private Boolean sendFlag = false;

    private String source;

    private String destination;

    private String copyDestinations;

    private String subject;

    private String sendResult;

    public Boolean getSendFlag() {
        return sendFlag;
    }

    public void setSendFlag(Boolean sendFlag) {
        this.sendFlag = sendFlag;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getCopyDestinations() {
        return copyDestinations;
    }

    public void setCopyDestinations(String copyDestinations) {
        this.copyDestinations = copyDestinations;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSendResult() {
        return sendResult;
    }

    public void setSendResult(String sendResult) {
        this.sendResult = sendResult;
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
                ", sendFlag=" + sendFlag +
                ", source='" + source + '\'' +
                ", destination='" + destination + '\'' +
                ", copyDestinations='" + copyDestinations + '\'' +
                ", subject='" + subject + '\'' +
                ", sendResult='" + sendResult + '\'' +
                '}';
    }
}
