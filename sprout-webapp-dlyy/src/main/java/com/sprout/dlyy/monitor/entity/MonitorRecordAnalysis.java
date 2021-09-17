package com.sprout.dlyy.monitor.entity;

/**
 * 考勤汇总统计
 */
public class MonitorRecordAnalysis {

    /**
     * 工号
     */
    private String workNo;

    /**
     * 员工姓名
     */
    private String workerName;

    /**
     * 正常次数
     */
    private int normalCount = 0;

    /**
     * 迟到次数
     */
    private int lateCount = 0;

    private int earlyCount = 0;
    /**
     * 缺勤次数
     */
    private int absenceCount = 0;


    public String getWorkNo() {
        return workNo;
    }

    public void setWorkNo(String workNo) {
        this.workNo = workNo;
    }

    public String getWorkerName() {
        return workerName;
    }

    public void setWorkerName(String workerName) {
        this.workerName = workerName;
    }

    public int getNormalCount() {
        return normalCount;
    }

    public void setNormalCount(int normalCount) {
        this.normalCount = normalCount;
    }

    public int getLateCount() {
        return lateCount;
    }

    public void setLateCount(int lateCount) {
        this.lateCount = lateCount;
    }

    public int getEarlyCount() {
        return earlyCount;
    }

    public void setEarlyCount(int earlyCount) {
        this.earlyCount = earlyCount;
    }

    public int getAbsenceCount() {
        return absenceCount;
    }

    public void setAbsenceCount(int absenceCount) {
        this.absenceCount = absenceCount;
    }

    @Override
    public String toString() {
        return "MonitorRecordAnalysis{" +
                "workNo='" + workNo + '\'' +
                ", workerName='" + workerName + '\'' +
                ", normalCount=" + normalCount +
                ", lateCount=" + lateCount +
                ", earlyCount=" + earlyCount +
                ", absenceCount=" + absenceCount +
                '}';
    }
}
