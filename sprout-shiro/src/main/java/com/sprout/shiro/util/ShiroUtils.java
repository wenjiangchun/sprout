package com.sprout.shiro.util;

import com.sprout.core.spring.SpringContextUtils;
import com.sprout.shiro.ShiroUser;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ShiroUtils {

    /**
     * 获取在线用户
     * @return
     */
    public static List<ShiroUser> getOnlineUserList() {
        List<ShiroUser> onlineList = new ArrayList<>();
        SessionDAO sessionDAO = SpringContextUtils.getBean(SessionDAO.class);
        Collection<Session> activeSessions = sessionDAO.getActiveSessions();
        for (Session activeSession : activeSessions) {
            SimplePrincipalCollection principalCollection;
            if (activeSession.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY) != null) {
                principalCollection = (SimplePrincipalCollection) activeSession
                        .getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
                ShiroUser user = (ShiroUser) principalCollection.getPrimaryPrincipal();
                user.setIp(activeSession.getHost());
                user.setStartLoginTime(activeSession.getStartTimestamp());
                onlineList.add(user);
            }
        }
        return onlineList;

    }

    /**
     * 获取当前登录用户
     * @return 当前登录用户
     */
    public static ShiroUser getCurrentUser() {
        Object currentUser = SecurityUtils.getSubject().getPrincipal();
        return currentUser != null ? (ShiroUser)currentUser: null;
    }
}
