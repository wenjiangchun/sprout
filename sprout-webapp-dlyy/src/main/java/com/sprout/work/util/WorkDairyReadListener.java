package com.sprout.work.util;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.sprout.common.util.SproutDateUtils;
import com.sprout.common.util.SproutStringUtils;
import com.sprout.system.entity.User;
import com.sprout.work.entity.DairySendConfig;
import com.sprout.work.entity.WorkDairy;
import com.sprout.work.service.WorkDairyService;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class WorkDairyReadListener extends AnalysisEventListener<WorkDairyWrapper> {

    private WorkDairyService workDairyService;

    private User worker;

    private DairySendConfig dairySendConfig;

    public WorkDairyReadListener(WorkDairyService workDairyService, DairySendConfig dairySendConfig, User worker) {
        this.workDairyService = workDairyService;
        this.worker = worker;
        this.dairySendConfig = dairySendConfig;
    }
    @Override
    public void invoke(WorkDairyWrapper data, AnalysisContext context) {
        //根据日期查询是否存在旧的日志信息
        try {
            List<WorkDairy> workDairyList = workDairyService.findByProperty("workDay", SproutDateUtils.parseDate(data.getWorkDay(), "yyyy-MM-dd"));
            WorkDairy workDairy = new WorkDairy();
            if (!workDairyList.isEmpty()) {
                workDairy = workDairyList.get(0);
            }
            Date workDay = SproutDateUtils.parseDate(data.getWorkDay(), "yyyy-MM-dd");
            WorkDayUtils.WeekDay weekDay = WorkDayUtils.getWeekDayByDate(workDay);
            if (SproutStringUtils.isBlank(data.getContent())) {
                //自动填充周末
                if (weekDay.equals(WorkDayUtils.WeekDay.SAT) || weekDay.equals(WorkDayUtils.WeekDay.SUN)) {
                    workDairy.setContent("周末");
                }
            } else {
                workDairy.setContent(data.getContent());
            }
            workDairy.setRemark(data.getRemark());
            workDairy.setWorker(worker);

            workDairy.setWorkDay(workDay);
            if (Objects.nonNull(this.dairySendConfig)) {
                //采用配置信息 如果小于开始时间则忽略不入库
                int weekStartNum = dairySendConfig.getWeekStartNum();
                Date weekStartDay = dairySendConfig.getDairyStartDay();
                if (workDay.before(weekStartDay)) {
                    //TODO 暂时不处理
                } else {
                    //另外计算所属周，星期信息
                    workDairy.setWeekDay(weekDay.getWeekDayName());
                    workDairy.setWeekNum(WorkDayUtils.getWeekNum(weekStartDay, workDay) + weekStartNum - 1);
                    workDairyService.save(workDairy);
                }
            } else {
                workDairy.setWeekDay(data.getWeekDay());
                workDairy.setWeekNum(data.getWeekNum());
                workDairyService.save(workDairy);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {

    }
}
