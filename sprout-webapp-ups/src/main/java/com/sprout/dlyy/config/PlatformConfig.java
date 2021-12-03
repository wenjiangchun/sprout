package com.sprout.dlyy.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "dlyy.env")
public class PlatformConfig {

    private boolean genExampleHost = false;

    private Double minBatteryPower;

    public boolean isGenExampleHost() {
        return genExampleHost;
    }

    public void setGenExampleHost(boolean genExampleHost) {
        this.genExampleHost = genExampleHost;
    }

    public Double getMinBatteryPower() {
        return minBatteryPower;
    }

    public void setMinBatteryPower(Double minBatteryPower) {
        this.minBatteryPower = minBatteryPower;
    }
}
