package com.sprout.dlyy.devops.ups;

import com.jcraft.jsch.JSchException;
import com.sprout.common.util.SproutDateUtils;
import com.sprout.dlyy.devops.host.service.ServerHostService;
import com.sprout.dlyy.devops.util.ControlType;
import com.sprout.dlyy.devops.util.SSHClient;
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

    private static NamedParameterJdbcTemplate jdbcTemplate;

    static {
        try {
            Class<Driver> driverClass = (Class<Driver>) Class.forName("com.mysql.jdbc.Driver");
            Driver driver = BeanUtils.instantiateClass(driverClass);
            DataSource dateSource = new SimpleDriverDataSource(driver,"jdbc:mysql://10.18.19.234:3306/agentobj", "root", "rooter");
            jdbcTemplate = new NamedParameterJdbcTemplate(dateSource);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Scheduled(cron = "0 */3 * * * ?")
    private void checkUpsStatus() {
        LOGGER.debug("市电监测执行扫描...");
        String sql = "select id,value,tm from devvou where id='Ups_Input_U' union all select id, value,tm from devvou where id='Ups_BAT_U';";
        Map<String,Object> params = new HashMap<>();
        List<Map<String, Object>> rs = jdbcTemplate.queryForList(sql, params);
        if (rs.size() == 2) { //正常数据为任一时刻两条对应数据, 其它情况则为异常
            Map<String, Object> upsIn = rs.get(0);
            double inValue = Double.parseDouble(upsIn.get("value").toString());
            Map<String, Object> upsOut = rs.get(1);
            double outValue = Double.parseDouble(upsOut.get("value").toString());
            LOGGER.info("动环数据监测结果，输入电压【{}】,输出电压【{}】", inValue, outValue);
            if (inValue == 0d && outValue <=240d) {
                //执行关机
                serverHostService.findAll().forEach(host -> {
                    //判断是通过SSH还是通过IPMI关机
                    try {
                        if (host.getControlType() == ControlType.IPMI) {

                        } else {
                            SSHClient sshClient = new SSHClient(host);
                            String result = sshClient.execCmd("shutdown -h now");
                            LOGGER.info("执行关机结果，主机【{}】,关机结果【{}】", host.getIp(), result);
                            sshClient.close();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        } else {
            LOGGER.error("动环数据监测异常，执行时间点【{}】", SproutDateUtils.format(new Date()));
        }
    }
}
