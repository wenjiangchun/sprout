package com.sprout.work.util;

import com.sprout.common.util.SproutStringUtils;
import com.sprout.work.entity.DairySendConfig;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.ConfigurableMimeFileTypeMap;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Date;
import java.util.Properties;

//@Component
public class EmailSender {

    //@Autowired
    //private JavaMailSenderImpl javaMailSender;

    public static void sendSimpleMail(DairySendConfig dairySendConfig, String subject, String content) {
        JavaMailSender sender = getJavaMailSender(dairySendConfig);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject(subject);
        message.setFrom(dairySendConfig.getSource());
        message.setTo(dairySendConfig.getDestination());
        String copyDestinations = dairySendConfig.getCopyDestinations();
        if (SproutStringUtils.isNotBlank(copyDestinations)) {
            message.setCc(copyDestinations.split(";"));
        }
        message.setSentDate(new Date());
        message.setText(content);
        sender.send(message);
    }

    public static void sendMimeMail(DairySendConfig dairySendConfig, String subject, String content, File addFile) throws MessagingException {
        JavaMailSender sender = getJavaMailSender(dairySendConfig);
        MimeMessage mimeMessage = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true);
        helper.setSubject(subject);
        helper.setFrom(dairySendConfig.getSource());
        helper.setTo(dairySendConfig.getDestination());
        String copyDestinations = dairySendConfig.getCopyDestinations();
        if (SproutStringUtils.isNotBlank(copyDestinations)) {
            helper.setCc(copyDestinations.split(";"));
        }
        helper.setSentDate(new Date());
        helper.setText(content);
        helper.addAttachment(dairySendConfig.getWorker().getName() + "第" + dairySendConfig.getWeekStartNum() + "周工作周报.xlsx", addFile);
        sender.send(mimeMessage);
    }

    private static JavaMailSender getJavaMailSender(DairySendConfig dairySendConfig) {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setProtocol("smtp");
        sender.setUsername(dairySendConfig.getSource());
        sender.setHost(dairySendConfig.getSmtp());
        sender.setPassword(dairySendConfig.getToken());
        sender.setDefaultEncoding("utf-8");
        sender.setDefaultFileTypeMap(new ConfigurableMimeFileTypeMap());
        Properties properties = new Properties();
        properties.setProperty("mail.smtp.ssl.enable", "true");
        sender.setJavaMailProperties(properties);
        return sender;
    }
}
