package com.sprout.work.entity;

import com.sprout.core.jpa.entity.AbstractBaseEntity;
import com.sprout.system.entity.User;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "d_holiday")
public class Holiday extends AbstractBaseEntity<Long> {

    private static final long serialVersionUID = 1L;

    private String workDay;

    private HolidayItem holidayItem;

    private int year;

    private int month;

    private int day;

    public String getWorkDay() {
        return workDay;
    }

    public void setWorkDay(String workDay) {
        this.workDay = workDay;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "holiday_item_id")
    public HolidayItem getHolidayItem() {
        return holidayItem;
    }

    public void setHolidayItem(HolidayItem holidayItem) {
        this.holidayItem = holidayItem;
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

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    @Override
    public String toString() {
        return "Holiday{" +
                "id=" + id +
                ", workDay=" + workDay +
                ", holidayItem=" + holidayItem +
                ", year=" + year +
                ", month=" + month +
                ", day=" + day +
                '}';
    }
}
