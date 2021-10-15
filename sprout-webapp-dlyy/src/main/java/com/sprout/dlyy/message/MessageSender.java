package com.sprout.dlyy.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Component;

@Component
public class MessageSender {

    private static final Logger logger = LoggerFactory.getLogger(MessageSender.class);

    private final AmqpAdmin amqpAdmin;

    private final AmqpTemplate amqpTemplate;

    public MessageSender(AmqpAdmin amqpAdmin, AmqpTemplate amqpTemplate) {
        this.amqpAdmin = amqpAdmin;
        this.amqpTemplate = amqpTemplate;
    }

    public void sendMessage(Object message) {
        this.amqpTemplate.convertAndSend("dlyy", null, message);
        logger.debug("发送消息到队列【{}】,消息【{}】", "dlyy", message);
    }
}
