package com.sprout.shiro.config;

import com.jagregory.shiro.freemarker.ShiroTags;
import freemarker.template.Configuration;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

@Component
public class ShiroFreemarkerTagConfig implements InitializingBean {

    private Configuration configuration;

    private FreeMarkerViewResolver freeMarkerViewResolver;

    public ShiroFreemarkerTagConfig(Configuration configuration, FreeMarkerViewResolver freeMarkerViewResolver) {
        this.configuration = configuration;
        this.freeMarkerViewResolver = freeMarkerViewResolver;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        configuration.setSharedVariable("shiro", new ShiroTags());
        freeMarkerViewResolver.setRequestContextAttribute("context");
    }
}
