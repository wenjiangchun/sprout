package com.sprout.oa.leave.web.controller;

import com.sprout.common.util.SproutDateUtils;
import com.sprout.core.spring.SpringContextUtils;
import com.sprout.oa.leave.entity.LeaveStatistic;
import com.sprout.oa.leave.service.LeaveStatisticService;
import com.sprout.system.entity.User;
import com.sprout.system.service.UserService;
import com.sprout.web.base.BaseCrudController;
import com.sprout.web.util.RestResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/oa/leaveStatistic")
public class LeaveStatisticController extends BaseCrudController<LeaveStatistic, Long> {

    private LeaveStatisticService leaveStatisticService;

    public LeaveStatisticController(LeaveStatisticService leaveStatisticService) {
        super("oa", "leaveStatistic", "请假汇总", leaveStatisticService);
        this.leaveStatisticService = leaveStatisticService;
    }

    @GetMapping("/getUserHolidayInfo/{userId}")
    @ResponseBody
    public List<LeaveStatistic> getUserHolidayInfo(@PathVariable Long userId) {
        //查询用户当年请假天数 包括年休假及事假
        User user = SpringContextUtils.getBean(UserService.class).findById(userId);
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("year", SproutDateUtils.getYear(new Date()));
        queryParams.put("applierId", userId);
        return this.leaveStatisticService.findByJql("from LeaveStatistic where year=:year and applier.id=:applierId", queryParams);
    }

}