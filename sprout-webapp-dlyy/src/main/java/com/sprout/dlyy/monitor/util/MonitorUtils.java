package com.sprout.dlyy.monitor.util;

import com.sprout.common.util.SproutDateUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class MonitorUtils {

    private MongoTemplate mongoTemplate;

    public MonitorUtils(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

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

    public List<String> getUserNameList() {
        //return mongoTemplate.
        List<String> nameList = new ArrayList<>();
        /*DistinctIterable distinctIterable = mongoTemplate.getCollection("workers_bace").distinct("name", String.class);
        MongoCursor cursor = distinctIterable.iterator();
        while (cursor.hasNext()) {
            String category = (String)cursor.next();
            nameList.add(category);
        }*/
        Query query = new Query();
        query.with(Sort.by(Sort.Order.asc("post_no")));
        List<Worker> workerList = mongoTemplate.find(query, Worker.class, "workers_bace");
        workerList.forEach(worker -> {
            nameList.add(worker.getName());
        });
        return nameList;
    }
}
