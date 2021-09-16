package com.sprout.system.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;

public class UserApplicationListener implements ApplicationListener<UserChangeGroupEvent> {

    private static final Logger logger = LoggerFactory.getLogger(UserApplicationListener.class);

    @Override
    public void onApplicationEvent(UserChangeGroupEvent event) {
        logger.debug("用户机构发生变化, user={}, group={}", event.getUser(), event.getUser().getGroup());
    }
}
