package com.sprout.dlyy.alarm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("alarm")
@Controller
public class AlarmController {

    @Autowired
    private JavaMailSenderImpl javaMailSender;

    @GetMapping("sendEmail")
    @ResponseBody
    public String sendEmail() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("机房监测");
        message.setText("通信正常");
        message.setTo("wenjiangchun@163.com");
        message.setFrom("271158251@qq.com");
        javaMailSender.send(message);
        return "发送成功";
    }
}
