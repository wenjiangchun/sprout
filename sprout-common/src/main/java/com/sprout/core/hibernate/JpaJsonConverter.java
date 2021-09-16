package com.sprout.core.hibernate;


import com.sprout.common.util.SproutJsonUtils;

import javax.persistence.AttributeConverter;

/**
 * 将字符串映射为数据库Json字段转换器
 */
public class JpaJsonConverter implements AttributeConverter<Object, String> {

    @Override
    public String convertToDatabaseColumn(Object attribute) {
        return SproutJsonUtils.writeToString(attribute);
    }

    @Override
    public Object convertToEntityAttribute(String value) {
        return SproutJsonUtils.readFromString(value, Object.class);
    }
}
