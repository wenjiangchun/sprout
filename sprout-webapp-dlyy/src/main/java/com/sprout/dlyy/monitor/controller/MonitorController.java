package com.sprout.dlyy.monitor.controller;

import com.alibaba.excel.EasyExcel;
import com.sprout.common.util.SproutDateUtils;
import com.sprout.dlyy.door.service.DoorService;
import com.sprout.dlyy.door.wrapper.Record;
import com.sprout.dlyy.kudu.KuduTableMeta;
import com.sprout.dlyy.kudu.KuduUtils;
import com.sprout.dlyy.monitor.entity.AttendanceConfig;
import com.sprout.dlyy.monitor.service.AttendanceConfigService;
import com.sprout.dlyy.monitor.util.MonitorRecord;
import com.sprout.dlyy.monitor.util.MonitorRecordAnalysis;
import com.sprout.dlyy.monitor.util.MonitorUtils;
import com.sprout.web.base.BaseController;
import com.sprout.web.util.WebMessage;
import org.apache.kudu.Schema;
import org.apache.kudu.client.KuduClient;
import org.apache.kudu.client.KuduException;
import org.apache.kudu.client.KuduTable;
import org.springframework.boot.actuate.autoconfigure.metrics.MetricsProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.*;

@Controller
@RequestMapping(value = "/dlyy/monitor")
public class MonitorController extends BaseController {

    private MonitorUtils monitorUtils;

    private AttendanceConfigService attendanceConfigService;

    private MonitorController(MonitorUtils monitorUtils, AttendanceConfigService attendanceConfigService) {
        this.monitorUtils = monitorUtils;
        this.attendanceConfigService = attendanceConfigService;
    }

    @GetMapping("/view")
    public String index(Model model) {
        List<String> nameList = monitorUtils.getUserNameList();
        model.addAttribute("nameList", nameList);
        //获取该月第一天日期
        model.addAttribute("firstDayOfCurrentMonth", SproutDateUtils.getFirstDayOfMonth());
        model.addAttribute("currentDayOfCurrentMonth", new Date());
        return "/dlyy/monitor/index";
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
        List<MonitorRecord> recordList = monitorUtils.findRecordList(start,end);
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
        List<MonitorRecord> recordList = monitorUtils.findRecordList(start,end);
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
        List<String> nameList = monitorUtils.getUserNameList();
        nameList.add(0, "日期");
        List<List<String>> dataWrapper = new ArrayList<>();
        for (String day : dayList) {
            List<String> dt = new ArrayList<>();
            dt.add(day);
            for (int i = 1; i < nameList.size(); i++) {
                String userName = nameList.get(i);
                for (MonitorRecord record : recordList) {
                    if (record.getRecordDate().equals(day) && record.getName().equals(userName)) {
                        String content = "";
                        String amState = record.getAmState();
                        String pmState = record.getPmState();
                        if (Objects.isNull(amState) && Objects.isNull(pmState)) {
                            content = "";
                        } else {
                            content = "上午：" + amState + "\n" +" 下午：" + pmState ;
                        }
                        dt.add(content);
                        break;
                    }
                }
            }

            int sstart = dt.size();
                for (int i = sstart; i < nameList.size(); i++) {
                    if (dt.size() < nameList.size()) {
                        dt.add("-");
                    } else {
                        break;
                    }
                }
            dataWrapper.add(dt);
        }
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        //URLEncoder.encode 防止中文乱码
        String fileName = URLEncoder.encode("视频打卡记录", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
       // EasyExcel.write(response.getOutputStream()).head(getHead(nameList)).registerWriteHandler(new CustomCellWriteHandler()).sheet("打卡记录").autoTrim(true).doWrite(dataWrapper);
        EasyExcel.write(response.getOutputStream()).head(getHead(nameList)).registerWriteHandler(CustomHorizontalCellStyleStrategy.create()).sheet("打卡记录").autoTrim(true).doWrite(dataWrapper);

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

    @GetMapping("/analysis")
    public String analysis(Model model) {
        List<String> nameList = monitorUtils.getUserNameList();
        model.addAttribute("nameList", nameList);
        //获取该月第一天日期
        model.addAttribute("firstDayOfCurrentMonth", SproutDateUtils.getFirstDayOfMonth());
        model.addAttribute("currentDayOfCurrentMonth", new Date());
        return "/dlyy/monitor/analysis";
    }

    @GetMapping("/getTotalCount")
    @ResponseBody
    public List<MonitorRecordAnalysis> getTotalCount(@RequestParam(required = false) String startDate, @RequestParam(required = false)  String endDate) {
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
        List<MonitorRecord> recordList = monitorUtils.findRecordList(start,end);
        List<String> nameList = monitorUtils.getUserNameList();
        Map<String, MonitorRecordAnalysis> rs = new LinkedHashMap<>();
        nameList.forEach(name-> {
            MonitorRecordAnalysis analysis = new MonitorRecordAnalysis();
            analysis.setWorkerName(name);
            rs.put(name, analysis);
        });
        recordList.forEach(monitorRecord -> {
            String userName = monitorRecord.getName();
            String amState = monitorRecord.getAmState();
            String pmState = monitorRecord.getPmState();
            //获取初始化员工汇总信息
            MonitorRecordAnalysis analysis = rs.get(userName);
            processState(amState, analysis);
            processState(pmState, analysis);
            //rs.put(userName, analysis);
        });
        return new ArrayList<>(rs.values());
    }

    private void processState(String state, MonitorRecordAnalysis analysis) {
        if (Objects.nonNull(state)) {
            if (state.contains("缺勤")) {
                analysis.setAbsenceCount(analysis.getAbsenceCount() + 1);
            }
            if (state.contains("迟到")) {
                analysis.setLateCount(analysis.getLateCount() + 1);
            }
            if (state.contains("正常")) {
                analysis.setNormalCount(analysis.getNormalCount() + 1);
            }
            if (state.contains("早退")) {
                analysis.setEarlyCount(analysis.getEarlyCount() + 1);
            }
        }
    }


    /**----------------------------------打卡规则配置------------------------------------**/
    @GetMapping("config")
    public String configView(Model model) {
        List<AttendanceConfig> attendanceConfigList = attendanceConfigService.findAll();
        model.addAttribute("configList", attendanceConfigList);
        return "/dlyy/monitor/config";
    }

    @GetMapping("editConfig/{id}")
    public String editConfig(Model model, @PathVariable String id) {
        model.addAttribute("config", attendanceConfigService.findById(id));
        return "/dlyy/monitor/editConfig";
    }

    @PostMapping("saveConfig")
    @ResponseBody
    public WebMessage saveConfig(Model model, AttendanceConfig config) {
        try {
            attendanceConfigService.saveConfig(config);
            return WebMessage.createSuccessWebMessage();
        } catch (Exception ex) {
            return WebMessage.createErrorWebMessage("编辑失败，请重试");
        }
    }
}
