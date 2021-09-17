package com.sprout.dlyy.monitor.service;

import com.sprout.common.util.SproutDateUtils;
import com.sprout.dlyy.monitor.entity.MonitorRecord;
import com.sprout.dlyy.monitor.entity.Worker;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 监控打卡记录业务类
 */
@Component
public class MonitorRecordService {

    private MongoTemplate mongoTemplate;

    public MonitorRecordService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * 根据起止日期获取监控打卡记录
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 打卡记录列表
     */
    public List<MonitorRecord> findRecordList(Date startDate, Date endDate) {
        Criteria criteria = new Criteria();
        Query query = new Query(
                criteria.andOperator(
                                Criteria.where("time_today").gte(SproutDateUtils.format(startDate, "yyyy-MM-dd")),
                                Criteria.where("time_today").lte(SproutDateUtils.format(endDate, "yyyy-MM-dd"))
                        )
        );
        return mongoTemplate.find(query, MonitorRecord.class, "workers_days");
    }

    /**
     * 获取打卡员工姓名列表 根据员工编号排序
     * @return 员工姓名列表
     */
    public List<String> getUserNameList() {
        List<String> nameList = new ArrayList<>();
        Query query = new Query();
        query.with(Sort.by(Sort.Order.asc("post_no")));
        List<Worker> workerList = mongoTemplate.find(query, Worker.class, "workers_bace");
        workerList.forEach(worker -> {
            nameList.add(worker.getName());
        });
        return nameList;
    }


}
