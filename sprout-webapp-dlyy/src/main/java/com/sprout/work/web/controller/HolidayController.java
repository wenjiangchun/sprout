package com.sprout.work.web.controller;

import com.sprout.common.util.SproutDateUtils;
import com.sprout.web.base.BaseCrudController;
import com.sprout.web.util.RestResult;
import com.sprout.work.entity.*;
import com.sprout.work.service.HolidayService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

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
    public Collection<Holiday> findAll() {
        Map<String, Holiday> holidayCache = this.holidayService.getHolidayCache(false);
        return holidayCache.values();
        //return this.holidayService.findAll(Sort.by(Sort.Direction.ASC, "workDay"));
    }

    @PostMapping("/saveHoliday")
    @ResponseBody
    public Holiday saveHoliday(String itemName, String workDay)  {
        try {
            return this.holidayService.saveHoliday(itemName, SproutDateUtils.format(new Date(Long.parseLong(workDay)), "yyyy-MM-dd"));
        } catch (Exception e) {
            return new Holiday();
        }
    }

    @GetMapping("/checkWorkDay")
    @ResponseBody
    public boolean checkWorkDay(@RequestParam String workDay) {
       List<Holiday> holidayList = this.holidayService.findByProperty("workDay", SproutDateUtils.format(new Date(Long.parseLong(workDay)), "yyyy-MM-dd"));
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

    @PostMapping("/updateHolidayItem")
    @ResponseBody
    public RestResult updateHolidayItem(String preName, String name) {
        try {
            this.holidayService.updateHolidayItem(preName, name);
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

    @GetMapping("/moveHoliday")
    @ResponseBody
    public RestResult moveHoliday(@RequestParam String sourceDay, @RequestParam String targetDay) {

        return null;
    }

    @GetMapping("/deleteHoliday")
    @ResponseBody
    public RestResult deleteHoliday(@RequestParam String workDay) {
        try {
            List<Holiday> targetList = this.holidayService.findByProperty("workDay", workDay);
            for (Holiday holiday : targetList) {
                this.holidayService.delete(holiday);
            }
            logger.debug("成功删除节假日【{}】", workDay);
            return RestResult.createSuccessResult("删除成功");
        } catch (Exception ex) {
            return RestResult.createErrorResult("删除失败:" + ex.getMessage());
        }
    }

    @GetMapping("generateHoliday")
    @ResponseBody
    public RestResult generateHoliday() {
        try {
            int holidayNum = this.holidayService.generateHoliday();
            String content = "";
            if (holidayNum > 0) {
                content += "批量生成节日数据成功,生成"+holidayNum+"条数据";
            } else {
                content += "数据已更新";
            }
            return RestResult.createSuccessResult(content);
        } catch (Exception e) {
            e.printStackTrace();
            return RestResult.createErrorResult("批量生成节日数据失败:" + e.getMessage());
        }

    }
}
