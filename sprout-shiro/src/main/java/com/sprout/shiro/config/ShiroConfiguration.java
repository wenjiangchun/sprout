package com.sprout.shiro.config;

import com.sprout.shiro.ShiroFilterChainDBDefinition;
import com.sprout.shiro.ShiroRealm;
import com.sprout.shiro.ShiroSessionListener;
import net.sf.ehcache.CacheManager;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Configuration
@PropertySource("classpath:/config/shiro/application.properties")
public class ShiroConfiguration {

    @Bean
    public ShiroRealm shiroRealm() {
        ShiroRealm realm =  new ShiroRealm();
        realm.setAuthenticationCacheName("shiroCache");
        realm.setAuthenticationCachingEnabled(true);
        realm.setCachingEnabled(true);
        return realm;
    }

    @Bean
    public ShiroFilterChainDefinition shiroFilterChainDefinition() {
        DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();
        chainDefinition.addPathDefinition("/login", "anon");
        chainDefinition.addPathDefinition("/actuator/**", "anon");
        chainDefinition.addPathDefinition("/wx/wxLogin", "anon");
        chainDefinition.addPathDefinition("/wx/login", "anon");
        chainDefinition.addPathDefinition("/res/**", "anon");
        chainDefinition.addPathDefinition("/resources/**", "anon");
        chainDefinition.addPathDefinition("/swagger-ui.html", "anon");
        chainDefinition.addPathDefinition("/swagger-resources/**", "anon");
        chainDefinition.addPathDefinition("/swagger-resources", "anon");
        chainDefinition.addPathDefinition("/v2/**", "anon");
        chainDefinition.addPathDefinition("/crsf", "anon");
        chainDefinition.addPathDefinition("/webjars/**", "anon");
        chainDefinition.addPathDefinition("/websocket/**", "anon");
        chainDefinition.addPathDefinition("/v/stat/testData", "anon");
        chainDefinition.addPathDefinition("/v/public/**", "anon");
        chainDefinition.addPathDefinition("/index", "authc");
        chainDefinition.addPathDefinitions(shiroFilterChainDBDefinition().getFilterChainDefinitions());
        chainDefinition.addPathDefinition("/**", "authc");

        return chainDefinition;
    }

    @Bean
    public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator(){
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator=new DefaultAdvisorAutoProxyCreator();
        /**
         * setUsePrefix(false)???????????????????????????bug????????????spring aop???????????????
         * ???@Controller??????????????????????????????@RequiresRole???shiro????????????????????????????????????????????????????????????404???
         * ?????????????????????????????????bug
         */
        defaultAdvisorAutoProxyCreator.setUsePrefix(true);
        return defaultAdvisorAutoProxyCreator;
    }

    /*@Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean() {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setLoginUrl("/login");
        shiroFilterFactoryBean.setSuccessUrl("/");
        shiroFilterFactoryBean.setUnauthorizedUrl("/");
        //shiroFilterFactoryBean.setFilterChainDefinitionMap(chainDefinitionSectionMetaSource().getObject());
        return shiroFilterFactoryBean;
    }*/

    @Bean
    public ShiroFilterChainDBDefinition shiroFilterChainDBDefinition() {
        return new ShiroFilterChainDBDefinition();
    }

    @Bean
    public EhCacheManager ehCacheManager(CacheManager cacheManager){
        EhCacheManager ehCacheManager =  new EhCacheManager();
        ehCacheManager.setCacheManager(cacheManager);
        return ehCacheManager;
    }

    /**
     * session ????????????
     *
     * @return DefaultWebSessionManager
     */
    @Bean
    public DefaultWebSessionManager sessionManager(SessionDAO sessionDAO) {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        List<SessionListener> listeners = new ArrayList<>();
        listeners.add(new ShiroSessionListener());
        //?????? session????????????
        sessionManager.setGlobalSessionTimeout(1800000);
        sessionManager.setSessionListeners(listeners);
        sessionManager.setSessionDAO(sessionDAO);
        sessionManager.setSessionIdUrlRewritingEnabled(false);
        sessionManager.setDeleteInvalidSessions(true);
        sessionManager.setSessionValidationSchedulerEnabled(true);
        sessionManager.setSessionIdCookieEnabled(true);
        return sessionManager;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    @Bean
    public DefaultWebSecurityManager securityManager(ShiroRealm shiroRealm, SessionDAO sessionDAO, CacheManager cacheManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // ?????? SecurityManager???????????? shiroRealm
        securityManager.setRealm(shiroRealm);
        // ?????? shiro session?????????
        securityManager.setSessionManager(sessionManager(sessionDAO));
        // ?????? ??????????????? cacheManager
        securityManager.setCacheManager(ehCacheManager(cacheManager));
        // ?????? rememberMeCookie
        securityManager.setRememberMeManager(rememberMeManager());
        return securityManager;
    }

    private SimpleCookie rememberMeCookie() {
        // ?????? cookie ??????????????? login.html ????????? <input type="checkbox" name="rememberMe"/>
        SimpleCookie cookie = new SimpleCookie("rememberMe");
        // ?????? cookie ??????????????????????????????????????????30??????
        cookie.setMaxAge(180000);
        return cookie;
    }

    /**
     * cookie????????????
     *
     * @return CookieRememberMeManager
     */
    private CookieRememberMeManager rememberMeManager() {
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(rememberMeCookie());
        // rememberMe cookie ???????????????
        String encryptKey = "haze_shiro_key";
        byte[] encryptKeyBytes = encryptKey.getBytes(StandardCharsets.UTF_8);
        String rememberKey =  Base64.getEncoder().encodeToString(encryptKeyBytes);
        cookieRememberMeManager.setCipherKey(Base64.getDecoder().decode(rememberKey));
        return cookieRememberMeManager;
    }

    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }
}
