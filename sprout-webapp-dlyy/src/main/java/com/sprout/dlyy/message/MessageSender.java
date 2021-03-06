package com.sprout.dlyy.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
        ExecutorService threadPool = Executors.newFixedThreadPool(20);
        /*for (int i = 0; i < 200000; i++) {
            String msg = message.toString() + i;
            threadPool.submit(() -> amqpTemplate.convertAndSend("dlyy", null, msg));
        }
        for (int i = 0; i < 200000; i++) {
            String msg = message.toString() + i;
            threadPool.submit(() -> amqpTemplate.convertAndSend("dlyy", null, msg));
        }
        for (int i = 0; i < 200000; i++) {
            String msg = message.toString() + i;
            threadPool.submit(() -> amqpTemplate.convertAndSend("dlyy", null, msg));
        }
        for (int i = 0; i < 200000; i++) {
            String msg = message.toString() + i;
            threadPool.submit(() -> amqpTemplate.convertAndSend("dlyy", null, msg));
        }
        for (int i = 0; i < 200000; i++) {
            String msg = message.toString() + i;
            threadPool.submit(() -> amqpTemplate.convertAndSend("dlyy", null, msg));
        }
        for (int i = 0; i < 200000; i++) {
            String msg = message.toString() + i;
            threadPool.submit(() -> amqpTemplate.convertAndSend("dlyy", null, msg));
        }
        for (int i = 0; i < 200000; i++) {
            String msg = message.toString() + i;
            threadPool.submit(() -> amqpTemplate.convertAndSend("dlyy", null, msg));
        }*/
        for (int i = 0; i < 200000; i++) {
            String msg = message.toString() + i;
            threadPool.submit(() -> amqpTemplate.convertAndSend("dlyy", null, msg));
        }
        logger.debug("????????????????????????{}???,?????????{}???", "dlyy", message);
    }
}
