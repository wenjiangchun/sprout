package com.sprout.dlyy.monitor.service;

import com.sprout.dlyy.monitor.entity.AttendanceConfig;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AttendanceConfigService {

    private MongoTemplate mongoTemplate;

    public AttendanceConfigService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public List<AttendanceConfig> findAll() {
        return mongoTemplate.findAll(AttendanceConfig.class);
    }

    public void saveConfig(AttendanceConfig config) {
        mongoTemplate.save(config);
    }

    public AttendanceConfig findById(String id) {
        return mongoTemplate.findById(new ObjectId(id), AttendanceConfig.class);
    }
}
