package com.sprout.work.entity;

import com.sprout.core.jpa.entity.AbstractBaseEntity;
import com.sprout.system.entity.User;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "d_dairy_send_config")
public class DairySendConfig extends AbstractBaseEntity<Long> {

    private static final long serialVersionUID = 1L;

    private String destination;

    private String source;

    private String token;

    private String smtp;

    private String copyDestinations;

    private User worker;

    private Date sendTime;

    private Date dairyStartDay;

    private int weekStartNum = 1;

    private String protocol;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSmtp() {
        return smtp;
    }

    public void setSmtp(String smtp) {
        this.smtp = smtp;
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

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    public Date getDairyStartDay() {
        return dairyStartDay;
    }

    public void setDairyStartDay(Date dairyStartDay) {
        this.dairyStartDay = dairyStartDay;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "worker_id")
    public User getWorker() {
        return worker;
    }

    public void setWorker(User worker) {
        this.worker = worker;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public int getWeekStartNum() {
        return weekStartNum;
    }

    public void setWeekStartNum(int weekStartNum) {
        this.weekStartNum = weekStartNum;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    @Override
    public String toString() {
        return "DairySendConfig{" +
                "id=" + id +
                ", destination='" + destination + '\'' +
                ", source='" + source + '\'' +
                ", token='" + token + '\'' +
                ", smtp='" + smtp + '\'' +
                ", copyDestinations='" + copyDestinations + '\'' +
                ", worker=" + worker +
                ", sendTime=" + sendTime +
                ", dairyStartDay=" + dairyStartDay +
                ", weekStartNum=" + weekStartNum +
                ", protocol='" + protocol + '\'' +
                '}';
    }
}
