package com.sprout.oa.leave.dao;

import com.sprout.core.jpa.repository.BaseRepository;
import com.sprout.oa.leave.entity.LeaveStatistic;
import org.springframework.stereotype.Repository;

@Repository
public interface LeaveStatisticDao extends BaseRepository<LeaveStatistic, Long> {

}
