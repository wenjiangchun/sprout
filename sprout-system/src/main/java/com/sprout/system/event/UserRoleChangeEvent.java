package com.sprout.system.event;

import com.sprout.system.entity.User;
import org.springframework.context.ApplicationEvent;


public class UserRoleChangeEvent extends ApplicationEvent {

    private User user;

    public UserRoleChangeEvent(User user) {
        super(user);
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
