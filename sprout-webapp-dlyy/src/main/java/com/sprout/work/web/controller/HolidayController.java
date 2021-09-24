package com.sprout.work.web.controller;

import com.sprout.web.base.BaseCrudController;
import com.sprout.web.util.RestResult;
import com.sprout.work.entity.*;
import com.sprout.work.service.HolidayService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
@RequestMapping(value = "/work/holiday")
public class HolidayController extends BaseCrudController<Holiday, Long> {

    private HolidayService holidayService;

    public HolidayController(HolidayService holidayService) {
        super("work", "holiday", "节假日", holidayService);
        this.holidayService = holidayService;
    }

    @Override
    protected void setPageQueryVariables(Map<String, Object> queryVariables, HttpServletRequest request) {

    }

    @Override
    protected void setModel(Model model, HttpServletRequest request) {
        super.setModel(model, request);
    }

    @GetMapping("/setHoliday")
    public String setHoliday() {
        return "/work/holiday/setHoliday";
    }

    @GetMapping("/findAll")
    @ResponseBody
    public List<Holiday> findAll() {
        return this.holidayService.findAll(Sort.by(Sort.Direction.ASC, "workDay"));
    }

    @PostMapping("/saveHoliday")
    @ResponseBody
    public RestResult saveHoliday(String itemName, String workDay) {
        try {
            this.holidayService.saveHoliday(itemName, new Date(Long.parseLong(workDay)));
            return RestResult.createSuccessResult("");
        } catch (Exception e) {
            return RestResult.createErrorResult(e.getMessage());
        }
    }

    @GetMapping("/checkWorkDay")
    @ResponseBody
    public boolean checkWorkDay(@RequestParam String workDay) {
       List<Holiday> holidayList = this.holidayService.findByProperty("workDay", new Date(Long.parseLong(workDay)));
       return holidayList.isEmpty();
    }

    @PostMapping("/saveHolidayItem")
    @ResponseBody
    public RestResult saveHolidayItem(HolidayItem holidayItem) {
        try {
            this.holidayService.saveHolidayItem(holidayItem);
            return RestResult.createSuccessResult("");
        } catch (Exception ex) {
            return RestResult.createErrorResult(ex.getMessage());
        }
    }

    @GetMapping("/getHolidayItemList")
    @ResponseBody
    public List<HolidayItem> getHolidayItemList() {
        return this.holidayService.getHolidayItemList();
    }
}
