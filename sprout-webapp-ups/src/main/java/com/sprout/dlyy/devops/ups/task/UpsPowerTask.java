package com.sprout.dlyy.devops.ups.task;

import com.sprout.common.util.SproutDateUtils;
import com.sprout.dlyy.config.PlatformConfig;
import com.sprout.devops.server.control.ServerRemoteControlContext;
import com.sprout.devops.server.control.ServerRemoteController;
import com.sprout.devops.server.entity.ServerHost;
import com.sprout.devops.server.entity.ServerLog;
import com.sprout.devops.server.service.ServerHostService;
import com.sprout.devops.server.service.ServerLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.sql.DataSource;
import java.sql.Driver;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@EnableScheduling
public class UpsPowerTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpsPowerTask.class);

    @Autowired
    private JavaMailSenderImpl javaMailSender;

    @Autowired
    private ServerHostService serverHostService;

    @Autowired
    private ServerLogService serverLogService;

    @Autowired
    private PlatformConfig platformConfig;

    private static NamedParameterJdbcTemplate jdbcTemplate;

    static {
        try {
            Class<Driver> driverClass = (Class<Driver>) Class.forName("com.mysql.jdbc.Driver");
            Driver driver = BeanUtils.instantiateClass(driverClass);
            DataSource dateSource = new SimpleDriverDataSource(driver, "jdbc:mysql://10.18.19.234:3306/agentobj", "root", "rooter");
            jdbcTemplate = new NamedParameterJdbcTemplate(dateSource);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Scheduled(cron = "0 */2 * * * ?")
    private void monitorUps() {
        LOGGER.debug("市电监测执行扫描...");
        String sql = "select id,value,tm from devvou where id='Ups_Input_U' union all select id, value,tm from devvou where id='Ups_BAT_U';";
        Map<String, Object> params = new HashMap<>();
        ServerLog serverLog = new ServerLog();
        serverLog.setExecTime(new Date());
        List<Map<String, Object>> rs = jdbcTemplate.queryForList(sql, params);
        if (rs.size() != 2) {
            serverLog.setMonitorResult("动环数据监测异常,请检查采集数据");
            LOGGER.error("动环数据监测异常，执行时间点【{}】", SproutDateUtils.format(new Date()));
        } else {
            //正常数据为任一时刻两条对应数据, 其它情况则为异常
            Map<String, Object> upsIn = rs.get(0);
            double inValue = Double.parseDouble(upsIn.get("value").toString());
            Map<String, Object> upsOut = rs.get(1);
            double outValue = Double.parseDouble(upsOut.get("value").toString());
            LOGGER.debug("动环数据监测结果，输入电压【{}】,输出电压【{}】", inValue, outValue);
            if (inValue == 0d && outValue <= platformConfig.getMinBatteryPower()) {
                //执行批量关机
                serverLog.setMonitorResult("市电断开，电量不足开始关机...");
                StringBuilder execResult = new StringBuilder();
                LOGGER.debug("开始批量关机...");
                for (ServerHost serverHost : serverHostService.findAll()) {
                    ServerRemoteController serverRemoteController = ServerRemoteControlContext.getServerController(serverHost);
                    try {
                        serverRemoteController.stopRemoteServer();
                        execResult.append(String.format("【%s】已关机", serverHost.getIp()));
                    } catch (Exception ex) {
                        execResult.append(String.format("【%s】关机失败", serverHost.getIp()));
                    }
                    execResult.append("\n");
                }
                serverLog.setExecResult(execResult.toString());
            } else {
                serverLog.setMonitorResult("监测正常");
                LOGGER.debug("动环数据监测正常，执行时间点【{}】", SproutDateUtils.format(new Date()));
            }
        }
        try {
            this.serverLogService.save(serverLog);
        } catch (Exception e) {
            LOGGER.error("动环数据日志保存失败【{}】，执行时间点【{}】", e.getMessage(), SproutDateUtils.format(new Date()));
        }
    }
}
