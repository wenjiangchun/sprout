package com.sprout.dlyy.door.web;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.sprout.common.util.SproutDateUtils;
import com.sprout.dlyy.door.service.DoorService;
import com.sprout.dlyy.door.wrapper.Record;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.*;

@Controller
@RequestMapping("/dlyy/door/")
public class DoorController {

    @GetMapping("/view")
    public String index(Model model) {
        List<String> nameList = DoorService.getPersonNameList();
        model.addAttribute("nameList", nameList);
        //获取该月第一天日期
        model.addAttribute("firstDayOfCurrentMonth", SproutDateUtils.getFirstDayOfMonth());
        model.addAttribute("currentDayOfCurrentMonth", new Date());
        return "/dlyy/door/index";
    }

    @GetMapping("/getRecordList")
    @ResponseBody
    public Map<String, Object> getRecordList(@RequestParam(required = false) String startDate, @RequestParam(required = false)  String endDate) {

        Date start = SproutDateUtils.getFirstDayOfMonth();
        Date end = new Date();
        try {
            if (startDate != null) {
                start = SproutDateUtils.parseDate(startDate, "yyyy-MM-dd");
            }
            if (endDate != null) {
                end = SproutDateUtils.parseDate(endDate, "yyyy-MM-dd");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        List<Record> recordList = DoorService.getRecordList(start,end);
        List<String> dayList = new ArrayList<>();
        //计算日期差
        dayList.add(SproutDateUtils.format(start, "yyyy-MM-dd"));
        boolean b = true;
        Date nextDay = start;
        while(b) {
            nextDay = SproutDateUtils.addDays(nextDay, 1);
            dayList.add(SproutDateUtils.format(nextDay, "yyyy-MM-dd"));
            if (SproutDateUtils.isSameDay(nextDay, end)) {
                b = false;
            }
        }
        Map<String, Object> rs = new HashMap<>();
        rs.put("recordList", recordList);
        rs.put("dayList", dayList);
        return rs;
    }

    @GetMapping("/exportExcel")
    public void exportExcel(@RequestParam(required = false) String startDate, @RequestParam(required = false)  String endDate, HttpServletResponse response) throws Exception {

        Date start = SproutDateUtils.getFirstDayOfMonth();
        Date end = new Date();
        try {
            if (startDate != null) {
                start = SproutDateUtils.parseDate(startDate, "yyyy-MM-dd");
            }
            if (endDate != null) {
                end = SproutDateUtils.parseDate(endDate, "yyyy-MM-dd");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        List<Record> recordList = DoorService.getRecordList(start,end);
        List<String> dayList = new ArrayList<>();
        //计算日期差
        dayList.add(SproutDateUtils.format(start, "yyyy-MM-dd"));
        boolean b = true;
        Date nextDay = start;
        while(b) {
            nextDay = SproutDateUtils.addDays(nextDay, 1);
            dayList.add(SproutDateUtils.format(nextDay, "yyyy-MM-dd"));
            if (SproutDateUtils.isSameDay(nextDay, end)) {
                b = false;
            }
        }
        List<String> nameList = DoorService.getPersonNameList();
        nameList.add(0, "日期");
        List<List<String>> dataWrapper = new ArrayList<>();
        for (String day : dayList) {
           List<String> dt = new ArrayList<>();
           dt.add(day);
            for (Record record : recordList) {
                String userName = nameList.get(dt.size());
                if (record.getRecordDate().equals(day) && record.getPersonName().equals(userName)) {
                    String onAm = record.getOnDutyAm();
                    String offAm = record.getOffDutyAm();
                    String onPm = record.getOnDutyPm();
                    String offPm = record.getOffDutyPm();
                    String content = "";
                    if (onAm.equals("-") && offAm.equals("-") && onPm.equals("-") && offPm.equals("-")) {
                        content = "";
                    } else {
                        content = "上午：" + onAm + "/" + offAm + (char) 10 +"下午：" + onPm + "/" + offPm ;
                    }
                    dt.add(content);
                }
                if (dt.size() == nameList.size()) {
                    break;
                }
            }

           dataWrapper.add(dt);
        }
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        //URLEncoder.encode 防止中文乱码
        String fileName = URLEncoder.encode("打卡记录", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream()).head(getHead(nameList)).sheet("打卡记录").autoTrim(true).doWrite(dataWrapper);

    }

    private List<List<String>> getHead(List<String> nameList) {
        List<List<String>> list = new ArrayList<>();
        for (String s : nameList) {
            List<String> head = new ArrayList<>();
            head.add(s);
            list.add(head);
        }
        return list;
    }
}
