package com.sprout.dlyy.student.entity;

import com.sprout.core.jpa.entity.AbstractBaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "y_paper")
public class Paper extends AbstractBaseEntity<Long>  {

    private String className;

    private String studentName;

    private Integer count;

    private String paperDay;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getPaperDay() {
        return paperDay;
    }

    public void setPaperDay(String paperDay) {
        this.paperDay = paperDay;
    }

    @Override
    public String toString() {
        return "Paper{" +
                "id=" + id +
                ", className='" + className + '\'' +
                ", studentName='" + studentName + '\'' +
                ", count=" + count +
                ", paperDay='" + paperDay + '\'' +
                '}';
    }
}
