package com.sprout.common.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;

/**
 * json工具类 主要用于json字符串和对象之间的转换
 * <strong>注意：</strong>json字符串中键值不能为单引号（''）应为双引号("")
 */
public final class SproutJsonUtils {

    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        // 转换为格式化的json
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        // 如果json中有新增的字段并且是实体类类中不存在的，不报错
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static <T> T readFromString(String json, Class<T> valueType) {
        try {
            return mapper.readValue(json, valueType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> String writeToString(T value) {
        try {
            return mapper.writeValueAsString(value);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
