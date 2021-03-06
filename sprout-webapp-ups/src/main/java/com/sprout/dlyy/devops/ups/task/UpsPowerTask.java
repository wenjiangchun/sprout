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
        LOGGER.debug("????????????????????????...");
        String sql = "select id,value,tm from devvou where id='Ups_Input_U' union all select id, value,tm from devvou where id='Ups_BAT_U';";
        Map<String, Object> params = new HashMap<>();
        ServerLog serverLog = new ServerLog();
        serverLog.setExecTime(new Date());
        List<Map<String, Object>> rs = jdbcTemplate.queryForList(sql, params);
        if (rs.size() != 2) {
            serverLog.setMonitorResult("????????????????????????,?????????????????????");
            LOGGER.error("?????????????????????????????????????????????{}???", SproutDateUtils.format(new Date()));
        } else {
            //?????????????????????????????????????????????, ????????????????????????
            Map<String, Object> upsIn = rs.get(0);
            double inValue = Double.parseDouble(upsIn.get("value").toString());
            Map<String, Object> upsOut = rs.get(1);
            double outValue = Double.parseDouble(upsOut.get("value").toString());
            LOGGER.debug("??????????????????????????????????????????{}???,???????????????{}???", inValue, outValue);
            if (inValue == 0d && outValue <= platformConfig.getMinBatteryPower()) {
                //??????????????????
                serverLog.setMonitorResult("???????????????????????????????????????...");
                StringBuilder execResult = new StringBuilder();
                LOGGER.debug("??????????????????...");
                for (ServerHost serverHost : serverHostService.findAll()) {
                    ServerRemoteController serverRemoteController = ServerRemoteControlContext.getServerController(serverHost);
                    try {
                        serverRemoteController.stopRemoteServer();
                        execResult.append(String.format("???%s????????????", serverHost.getIp()));
                    } catch (Exception ex) {
                        execResult.append(String.format("???%s???????????????", serverHost.getIp()));
                    }
                    execResult.append("\n");
                }
                serverLog.setExecResult(execResult.toString());
            } else {
                serverLog.setMonitorResult("????????????");
                LOGGER.debug("?????????????????????????????????????????????{}???", SproutDateUtils.format(new Date()));
            }
        }
        try {
            this.serverLogService.save(serverLog);
        } catch (Exception e) {
            LOGGER.error("?????????????????????????????????{}????????????????????????{}???", e.getMessage(), SproutDateUtils.format(new Date()));
        }
    }
}
