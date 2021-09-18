package com.sprout.work.util;

import com.sprout.common.util.SproutDateUtils;

import java.util.Date;

public final class WorkDayUtils {

    /**
     * 获取当前距离开始所在第几周
     * @param initialDate 开始日期
     * @param currentDate 当前日期
     * @return 当前日期所在周数
     */
    public static int getWeekNum(Date initialDate, Date currentDate) {
        WeekDay startWeekDay = getWeekDayByDate(initialDate);
        WeekDay currentWeekDay = getWeekDayByDate(currentDate);
        //获取两个日期之间的天数
        int days = SproutDateUtils.getDiff(initialDate, currentDate);
        //计算第一周差几天到达周日
        int nextWeekDay = 0;
        switch (startWeekDay) {
            case MON:
                nextWeekDay = 6;
                break;
            case TUE:
                nextWeekDay = 5;
                break;
            case WED:
                nextWeekDay = 4;
                break;
            case THU:
                nextWeekDay = 3;
                break;
            case FRI:
                nextWeekDay = 2;
                break;
            case SAT:
                nextWeekDay = 1;
                break;
            default:
        }
        if (days <= nextWeekDay) {
            return 1;
        } else {
            int leftDays = days - nextWeekDay;
            //剩余天数取莫
            if (leftDays % 7 == 0) {
                return 1 + leftDays / 7;
            } else {
                return 1 + leftDays / 7 + 1;
            }
        }
    }


    /**
     * 计算工作日属于周几
     * @param workDay 工作日
     * @return 星期几
     */
    public static WeekDay getWeekDayByDate(Date workDay) {
        int dayOfWeek = SproutDateUtils.getDayInWeek(workDay);
        switch (dayOfWeek) {
            case 1:
                return WeekDay.SUN;
            case 3:
                return WeekDay.TUE;
            case 4:
                return WeekDay.WED;
            case 5:
                return WeekDay.THU;
            case 6:
                return WeekDay.FRI;
            case 7:
                return WeekDay.SAT;
            default:
                return WeekDay.MON;
        }
    }

    public enum WeekDay {

        SUN("星期天"),

        MON("星期一"),

        TUE("星期二"),

        WED("星期三"),

        THU("星期四"),

        FRI("星期五"),

        SAT("星期六");

        private final String weekDayName;

        WeekDay(String weekDayName) {
            this.weekDayName = weekDayName;
        }

        public String getWeekDayName() {
            return weekDayName;
        }
    }
}
