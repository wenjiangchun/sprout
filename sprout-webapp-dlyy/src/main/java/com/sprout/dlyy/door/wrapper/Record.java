package com.sprout.dlyy.door.wrapper;

import java.io.Serializable;
import java.util.Date;


public class Record implements Serializable {

    private Long id;

    private String recordDate;

    private String onDutyAm;

    private String offDutyAm;

    private String onDutyPm;

    private String offDutyPm;

    private Float workLong;

    private Float workDays;

    private Long personId;

    private String personName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPersonId() {
        return personId;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(String recordDate) {
        this.recordDate = recordDate;
    }

    public String getOnDutyAm() {
        return onDutyAm;
    }

    public void setOnDutyAm(String onDutyAm) {
        this.onDutyAm = onDutyAm;
    }

    public String getOffDutyAm() {
        return offDutyAm;
    }

    public void setOffDutyAm(String offDutyAm) {
        this.offDutyAm = offDutyAm;
    }

    public String getOnDutyPm() {
        return onDutyPm;
    }

    public void setOnDutyPm(String onDutyPm) {
        this.onDutyPm = onDutyPm;
    }

    public String getOffDutyPm() {
        return offDutyPm;
    }

    public void setOffDutyPm(String offDutyPm) {
        this.offDutyPm = offDutyPm;
    }

    public Float getWorkLong() {
        return workLong;
    }

    public void setWorkLong(Float workLong) {
        this.workLong = workLong;
    }

    public Float getWorkDays() {
        return workDays;
    }

    public void setWorkDays(Float workDays) {
        this.workDays = workDays;
    }


    @Override
    public String toString() {
        return "Record{" +
                "id=" + id +
                ", recordDate=" + recordDate +
                ", onDutyAm='" + onDutyAm + '\'' +
                ", offDutyAm='" + offDutyAm + '\'' +
                ", onDutyPm='" + onDutyPm + '\'' +
                ", offDutyPm='" + offDutyPm + '\'' +
                ", workLong=" + workLong +
                ", workDays=" + workDays +
                ", personId=" + personId +
                ", personName='" + personName + '\'' +
                '}';
    }
}
