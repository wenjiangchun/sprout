package com.sprout.shiro.listener;

import com.sprout.core.spring.SpringContextUtils;
import com.sprout.shiro.ShiroRealm;
import com.sprout.shiro.ShiroUser;
import com.sprout.system.event.UserRoleChangeEvent;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;

public class UserRoleChangeListener implements ApplicationListener<UserRoleChangeEvent> {

    private static final Logger logger = LoggerFactory.getLogger(UserRoleChangeListener.class);

    @Override
    public void onApplicationEvent(UserRoleChangeEvent event) {
        logger.debug("检测用户角色发生变化, user={}, role={}", event.getUser(), event.getUser().getRoles());
        //获取对应用户信息 然后清空对应授权缓存
        ShiroRealm shiroRealm = SpringContextUtils.getBean(ShiroRealm.class);
        shiroRealm.getAuthenticationCache().remove(event.getUser().getLoginName());
        shiroRealm.getAuthenticationCache().clear();
        RealmSecurityManager rsm = (RealmSecurityManager) SecurityUtils.getSecurityManager();
        //AccountAuthorizationRealm为在项目中定义的realm类
        ShiroRealm shiroRealm1 = (ShiroRealm) rsm.getRealms().iterator().next();
        shiroRealm1.getAuthenticationCache().clear();

        SessionDAO sessionDao = SpringContextUtils.getBean(SessionDAO.class);
        for (Session activeSession : sessionDao.getActiveSessions()) {
            logger.debug("当前在线用户{}", activeSession);
            SimplePrincipalCollection principalCollection;
            if (activeSession.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY) == null) {
                continue;
            } else {
                principalCollection = (SimplePrincipalCollection) activeSession
                        .getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
                ShiroUser user = (ShiroUser) principalCollection.getPrimaryPrincipal();
                logger.debug("当前在线用户{}", user.name);
            }
        }
    }
}
