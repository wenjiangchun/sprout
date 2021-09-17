package com.sprout.dlyy.monitor.controller;

import com.alibaba.excel.EasyExcel;
import com.sprout.common.util.SproutDateUtils;
import com.sprout.dlyy.monitor.entity.AttendanceConfig;
import com.sprout.dlyy.monitor.service.AttendanceConfigService;
import com.sprout.dlyy.monitor.entity.MonitorRecord;
import com.sprout.dlyy.monitor.entity.MonitorRecordAnalysis;
import com.sprout.dlyy.monitor.service.MonitorRecordService;
import com.sprout.dlyy.monitor.util.CustomHorizontalCellStyleStrategy;
import com.sprout.dlyy.monitor.util.DutyState;
import com.sprout.web.base.BaseController;
import com.sprout.web.util.WebMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.*;

/**
 * 视频打卡Web处理类
 */
@Controller
@RequestMapping(value = "/dlyy/monitor")
public class MonitorController extends BaseController {

    private MonitorRecordService monitorRecordService;

    private AttendanceConfigService attendanceConfigService;

    private MonitorController(MonitorRecordService monitorRecordService, AttendanceConfigService attendanceConfigService) {
        this.monitorRecordService = monitorRecordService;
        this.attendanceConfigService = attendanceConfigService;
    }

    @GetMapping("/view")
    public String index(Model model) {
        List<String> nameList = monitorRecordService.getUserNameList();
        model.addAttribute("nameList", nameList);
        //获取该月第一天日期
        model.addAttribute("firstDayOfCurrentMonth", SproutDateUtils.getFirstDayOfMonth());
        model.addAttribute("currentDayOfCurrentMonth", new Date());
        return "/dlyy/monitor/index";
    }

    @GetMapping("/getRecordList")
    @ResponseBody
    public Map<String, Object> getRecordList(@RequestParam(required = false) String startDate, @RequestParam(required = false) String endDate) {
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
        List<MonitorRecord> recordList = monitorRecordService.findRecordList(start, end);
        List<String> dayList = new ArrayList<>();
        //计算日期差
        dayList.add(SproutDateUtils.format(start, "yyyy-MM-dd"));
        boolean b = true;
        Date nextDay = start;
        while (b) {
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

    /**
     * 根据起止日期导出员工每日打卡记录
     * @param startDate  开始日期 格式 "yyyy-MM-dd"
     * @param endDate    结束日期 格式 "yyyy-MM-dd"
     * @param response   httpResponse
     * @throws Exception
     */
    @GetMapping("/exportExcel")
    public void exportExcel(@RequestParam(required = false) String startDate, @RequestParam(required = false) String endDate, HttpServletResponse response) throws Exception {
        List<Date> dateList = processDate(startDate, endDate);
        Date start = dateList.get(0);
        Date end = dateList.get(1);
        List<MonitorRecord> recordList = monitorRecordService.findRecordList(start, end);
        List<String> dayList = new ArrayList<>();
        //计算日期差
        dayList.add(SproutDateUtils.format(start, "yyyy-MM-dd"));
        //是否循环完毕标志
        boolean b = true;
        Date nextDay = start;
        while (b) {
            nextDay = SproutDateUtils.addDays(nextDay, 1);
            dayList.add(SproutDateUtils.format(nextDay, "yyyy-MM-dd"));
            if (SproutDateUtils.isSameDay(nextDay, end)) {
                b = false;
            }
        }
        List<String> nameList = monitorRecordService.getUserNameList();
        nameList.add(0, "日期");
        List<List<String>> dataWrapper = new ArrayList<>();
        //根据日期组装打卡记录数据
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
                            content = "上午：" + amState + "\n" + " 下午：" + pmState;
                        }
                        dt.add(content);
                        break;
                    }
                }
            }
            int size = dt.size();
            for (int i = size; i < nameList.size(); i++) {
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

    /**
     * 生成打卡人员表头数据
     * @param nameList 人员姓名列表
     * @return 符合EasyExcel的数据列表
     */
    private List<List<String>> getHead(List<String> nameList) {
        List<List<String>> list = new ArrayList<>();
        for (String s : nameList) {
            List<String> head = new ArrayList<>();
            head.add(s);
            list.add(head);
        }
        return list;
    }

    /**
     * 进入打卡记录汇总界面
     * @param model 数据模型绑定对象
     * @return 汇总页面位置信息
     */
    @GetMapping("/analysis")
    public String analysis(Model model) {
        List<String> nameList = monitorRecordService.getUserNameList();
        model.addAttribute("nameList", nameList);
        //获取该月第一天日期
        model.addAttribute("firstDayOfCurrentMonth", SproutDateUtils.getFirstDayOfMonth());
        model.addAttribute("currentDayOfCurrentMonth", new Date());
        return "/dlyy/monitor/analysis";
    }

    /**
     * 根据起止日期获取人员考勤汇总记录
     * @param startDate 开始日期 格式 "yyyy-MM-dd"
     * @param endDate   结束日期 格式 "yyyy-MM-dd"
     * @return 汇总统计对象列表 @see MonitorRecordAnalysis
     */
    @GetMapping("/getTotalCount")
    @ResponseBody
    public List<MonitorRecordAnalysis> getTotalCount(@RequestParam(required = false) String startDate, @RequestParam(required = false) String endDate) {
        List<Date> dateList = processDate(startDate, endDate);
        Date start = dateList.get(0);
        Date end = dateList.get(1);
        List<MonitorRecord> recordList = monitorRecordService.findRecordList(start, end);
        List<String> nameList = monitorRecordService.getUserNameList();
        Map<String, MonitorRecordAnalysis> rs = new LinkedHashMap<>();
        nameList.forEach(name -> {
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

    /**
     * 转换日期数据，如果开始日期为空则取开始日期为每个月第一天，如果结束日期为空则取当天日期为结束日期.
     * @param startDate 开始日期 格式 "yyyy-mm-dd"
     * @param endDate   开始日期 格式 "yyyy-mm-dd"
     * @return   包含开始日期和结束日期的时间列表.
     */
    private List<Date> processDate(String startDate, String endDate) {
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
        return Arrays.asList(start, end);
    }

    /**
     * 根据打卡状态计算统计结果
     * @param state 状态值
     * @param analysis 打卡统计对象
     */
    private void processState(String state, MonitorRecordAnalysis analysis) {
        if (Objects.nonNull(state)) {
            if (state.contains(DutyState.ABSENCE.getState())) {
                analysis.setAbsenceCount(analysis.getAbsenceCount() + 1);
            }
            if (state.contains(DutyState.COME_LATE.getState())) {
                analysis.setLateCount(analysis.getLateCount() + 1);
            }
            if (state.contains(DutyState.NORMAL.getState())) {
                analysis.setNormalCount(analysis.getNormalCount() + 1);
            }
            if (state.contains(DutyState.LEAVE_EARLY.getState())) {
                analysis.setEarlyCount(analysis.getEarlyCount() + 1);
            }
        }
    }


    /**
     * ----------------------------------打卡规则配置------------------------------------
     **/
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
    public WebMessage saveConfig(AttendanceConfig config) {
        try {
            attendanceConfigService.saveConfig(config);
            return WebMessage.createSuccessWebMessage();
        } catch (Exception ex) {
            return WebMessage.createErrorWebMessage("编辑失败，请重试");
        }
    }



}
