package com.sprout.oa.leave.util;

import javax.persistence.AttributeConverter;

/**
 * 请假状态转换类
 */
public class LeaveStateConverter implements AttributeConverter<LeaveState, Integer> {

    @Override
    public Integer convertToDatabaseColumn(LeaveState attribute) {
        if (attribute == null) {
            throw new RuntimeException("LeaveState can not be null!");
        }
        return attribute.getState();
    }

    @Override
    public LeaveState convertToEntityAttribute(Integer dbData) {
        for (LeaveState state : LeaveState.values()) {
            if (state.getState() == dbData) {
                return state;
            }
        }
        throw new RuntimeException("unknown leave state value:" + dbData);
    }


}
