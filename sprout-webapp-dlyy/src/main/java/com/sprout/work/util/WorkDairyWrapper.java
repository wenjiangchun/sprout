package com.sprout.work.util;

import com.alibaba.excel.annotation.ExcelProperty;

public class WorkDairyWrapper {

    @ExcelProperty("日期")
    private String workDay;

    @ExcelProperty("星期")
    private String weekDay;

    @ExcelProperty("周数")
    private int weekNum;

    @ExcelProperty("内容")
    private String content;

    @ExcelProperty("备注")
    private String remark;

    public String getWorkDay() {
        return workDay;
    }

    public void setWorkDay(String workDay) {
        this.workDay = workDay;
    }

    public String getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(String weekDay) {
        this.weekDay = weekDay;
    }

    public int getWeekNum() {
        return weekNum;
    }

    public void setWeekNum(int weekNum) {
        this.weekNum = weekNum;
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
}
