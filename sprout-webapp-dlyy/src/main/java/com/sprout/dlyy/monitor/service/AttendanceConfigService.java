package com.sprout.dlyy.monitor.service;

import com.sprout.dlyy.monitor.entity.AttendanceConfig;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 打卡规则配置业务类
 */
@Service
public class AttendanceConfigService {

    private MongoTemplate mongoTemplate;

    public AttendanceConfigService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * 获取所有打卡规则
     * @return 打卡规则列表
     */
    public List<AttendanceConfig> findAll() {
        return mongoTemplate.findAll(AttendanceConfig.class);
    }

    /**
     * 保存打卡规则信息
     * @param config 规则配置信息
     */
    public void saveConfig(AttendanceConfig config) {
        mongoTemplate.save(config);
    }

    /**
     * 根据ID查找对应打卡规则
     * @param id 规则配置ID
     * @return 规则配置信息
     */
    public AttendanceConfig findById(String id) {
        return mongoTemplate.findById(new ObjectId(id), AttendanceConfig.class);
    }
}
