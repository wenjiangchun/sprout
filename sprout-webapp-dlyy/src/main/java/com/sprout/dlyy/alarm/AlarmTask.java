package com.sprout.dlyy.alarm;

import com.sprout.common.util.SproutDateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableScheduling
public class AlarmTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(AlarmTask.class);
    @Autowired
    private JavaMailSenderImpl javaMailSender;

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    /*@Autowired
    private EmailConfig emailConfig;*/

    /**
     * 5分钟执行一次
     */
    @Scheduled(cron = "0 */5 * * * ?")
    private void checkUpsStatus() {
        LOGGER.debug("市电监测执行扫描...");
        String sql = "select count(1) from devvou where TM >=:startTime and DevName='UPS1' and ID='Ups_Alarm_Electricity'";
        Date d = new Date();
        Map<String,Object> params = new HashMap<>();
        params.put("startTime", SproutDateUtils.addMinutes(d, -5));
        //int count = jdbcTemplate.queryForObject(sql, params, Integer.class);
        int count = 0;
        if (count > 0) {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setSubject("机房监测");
            message.setText("UPS市电故障,发送时间:" + SproutDateUtils.format(new Date()));
            //message.setTo(emailConfig.getDestination());
            message.setFrom(javaMailSender.getUsername());
            javaMailSender.send(message);
            //LOGGER.debug("发送市电报警邮件: From【{}】,To【{}】", javaMailSender.getUsername(), emailConfig.getDestination());
        }
    }
}
