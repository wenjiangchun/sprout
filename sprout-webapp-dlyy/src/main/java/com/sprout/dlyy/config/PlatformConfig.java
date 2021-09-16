package com.sprout.dlyy.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "dlyy.env")
public class PlatformConfig {

    private String impalaUrl;

    public String getImpalaUrl() {
        return impalaUrl;
    }

    public void setImpalaUrl(String impalaUrl) {
        this.impalaUrl = impalaUrl;
    }
}
