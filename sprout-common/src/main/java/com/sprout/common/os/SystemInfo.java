package com.sprout.common.os;

public class SystemInfo {
    private String key;
    private String value;
    private String description;

    public SystemInfo(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public SystemInfo(String key, String value, String description) {
        this.key = key;
        this.value = value;
        this.description = description;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
