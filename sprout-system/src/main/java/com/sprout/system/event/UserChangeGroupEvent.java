package com.sprout.system.event;

import com.sprout.system.entity.User;
import org.springframework.context.ApplicationEvent;


public class UserChangeGroupEvent extends ApplicationEvent {

    private User user;
    public UserChangeGroupEvent(User user) {
        super(user);
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
