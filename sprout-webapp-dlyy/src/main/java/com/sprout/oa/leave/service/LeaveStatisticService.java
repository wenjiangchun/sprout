package com.sprout.oa.leave.service;

import com.sprout.common.util.SproutDateUtils;
import com.sprout.core.service.AbstractBaseService;
import com.sprout.oa.leave.dao.LeaveStatisticDao;
import com.sprout.oa.leave.entity.Leave;
import com.sprout.oa.leave.entity.LeaveStatistic;
import com.sprout.oa.leave.util.DaySection;
import com.sprout.oa.leave.util.LeaveState;
import com.sprout.system.entity.Dict;
import com.sprout.system.entity.User;
import com.sprout.work.entity.Holiday;
import com.sprout.work.service.HolidayService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class LeaveStatisticService extends AbstractBaseService<LeaveStatistic, Long> {

    private LeaveStatisticDao leaveStatisticDao;

    private HolidayService holidayService;

    public LeaveStatisticService(LeaveStatisticDao leaveStatisticDao, HolidayService holidayService) {
        super(leaveStatisticDao);
        this.leaveStatisticDao = leaveStatisticDao;
        this.holidayService = holidayService;
    }

    @Transactional(readOnly = true)
    public float getTotalDays(Long userId, int year, int month) {
        List<LeaveStatistic> leaveStatisticList = this.leaveStatisticDao.getLeaveStatisticByApplierIdAndYearAndMonth(userId, year, month);
        float days = 0;
        for (LeaveStatistic leaveStatistic : leaveStatisticList) {
            days += leaveStatistic.getDays();
        }
        return days;
    }

    /**
     * 统计本次请假信息 如果跨月则分月统计 如果请假流程未结束则不能计算统计信息
     * @param leave 请假信息
     */
    public void saveLeaveStatistic(Leave leave) {
        if (leave.getState() != LeaveState.DONE) {
            throw new RuntimeException("流程未结束,不能计算统计信息");
        }
        Date startTime = leave.getRealStartTime();
        Date endTime = leave.getRealEndTime();
        //计算两个日期之间节假日
        List<Holiday> holidayList = this.holidayService.getHolidayList(SproutDateUtils.format(startTime, SproutDateUtils.DAY_PATTERN), SproutDateUtils.format(endTime, SproutDateUtils.DAY_PATTERN));
        //每一天执行一个查询更新 排除节假日
        Map<String, Float> resultMap = new HashMap<>();
        //采用年份 + 月份作为key
        if (SproutDateUtils.isSameDay(startTime, endTime)) { //起止时间是同一天
            if (holidayList.isEmpty()) { //请假当天为节假日 直接返回0
                if (leave.getRealStartFlag() == DaySection.ALL.getSection() || leave.getRealEndFlag() == DaySection.ALL.getSection()) {
                    resultMap.put(getDateKey(startTime), 1f);
                } else if (leave.getRealStartFlag() == DaySection.AM.getSection() && leave.getRealEndFlag() == DaySection.PM.getSection()) {
                    resultMap.put(getDateKey(startTime), 1f);
                } else {
                    resultMap.put(getDateKey(startTime), 0.5f);
                }
            }
        } else { //起止时间不为同一天
            //判断开始日期是否位于节假日
            boolean flag = true;
            while(flag) {
                if (startTime.after(endTime)) {
                    break;
                }
                if (!this.holidayService.existHoliday(SproutDateUtils.format(startTime, SproutDateUtils.DAY_PATTERN), holidayList)) {
                    if (leave.getRealStartFlag() == DaySection.ALL.getSection()) {
                        String key = getDateKey(startTime);
                        if (resultMap.containsKey(key)) {
                            resultMap.put(key, resultMap.get(key) + 1f);
                        } else {
                            resultMap.put(getDateKey(startTime), 1f);
                        }

                    } else {
                        String key = getDateKey(startTime);
                        if (resultMap.containsKey(key)) {
                            resultMap.put(key, resultMap.get(key) + 0.5f);
                        } else {
                            resultMap.put(getDateKey(startTime), 0.5f);
                        }
                    }
                }
                startTime = SproutDateUtils.addDays(startTime, 1);
                if (SproutDateUtils.isSameDay(startTime, endTime)) {
                    if (!this.holidayService.existHoliday(SproutDateUtils.format(endTime, SproutDateUtils.DAY_PATTERN), holidayList)) {
                        if (leave.getRealEndFlag() == DaySection.ALL.getSection()) {
                            String key = getDateKey(endTime);
                            if (resultMap.containsKey(key)) {
                                resultMap.put(key, resultMap.get(key) + 1f);
                            } else {
                                resultMap.put(getDateKey(endTime), 1f);
                            }
                        } else {
                            String key = getDateKey(endTime);
                            if (resultMap.containsKey(key)) {
                                resultMap.put(key, resultMap.get(key) + 0.5f);
                            } else {
                                resultMap.put(getDateKey(endTime), 0.5f);
                            }
                        }
                    }
                    flag = false;
                }
            }
        }

        //计算resultMap 循环插入
        Dict leaveType = leave.getLeaveType();
        User applier = leave.getApplier();
        for (String key : resultMap.keySet()) {
            int year = Integer.parseInt(key.split(",")[0]);
            int month = Integer.parseInt(key.split(",")[1]);
            //查询数据库是否存在记录
            LeaveStatistic statistic = this.leaveStatisticDao.getLeaveStatisticByApplierAndLeaveTypeAndYearAndMonth(applier, leaveType, year, month);
            if (Objects.nonNull(statistic)) {
                statistic.setDays(statistic.getDays() + resultMap.get(key));
            } else {
                statistic = new LeaveStatistic();
                statistic.setApplier(applier);
                statistic.setLeaveType(leaveType);
                statistic.setYear(year);
                statistic.setMonth(month);
                statistic.setDays(resultMap.get(key));
            }
            this.leaveStatisticDao.save(statistic);
        }
    }

    private String getDateKey(Date dayTime) {
        int year = SproutDateUtils.getYear(dayTime);
        int month = SproutDateUtils.getMonth(dayTime);
        return year + "," + month;
    }


    /**
     * 根据用户ID和年份查询对应请假统计信息,如果用户ID为空则查询所有用户,year为空查询所有年份数据
     * @param userId 用户ID
     * @param year 请假年份
     * @return [请假天数,请假年份,请假月份,请假类型,请假人]列表
     */
    @Transactional(readOnly = true)
    @SuppressWarnings ( "unchecked" )
    public List<Object[]> getLeaveStatisticList(Long userId, Integer year) {
        Map<String, Object> queryParams = new HashMap<>();
        String sql = "select ls.days, ls.year, ls.month, sd.name as dict_name ,su.name as user_name from wk_leave_statistic ls left join sys_dict sd on ls.leave_type_id = sd.id left join sys_user su on ls.applier_id = su.id where 1=1";
        if (Objects.nonNull(userId)) {
            sql += " and su.id=:userId";
            queryParams.put("userId", userId);
        }
        if (Objects.nonNull(year)) {
            sql += " and ls.year=:year";
            queryParams.put("year", year);
        }
        sql += " order by ls.year desc, ls.month desc";
        return (List<Object[]>) this.findBySql(sql, queryParams);
    }
}
