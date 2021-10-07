package com.sprout.oa.leave.dao;

import com.sprout.core.jpa.repository.BaseRepository;
import com.sprout.oa.leave.entity.LeaveTaskLog;
import org.springframework.stereotype.Repository;

@Repository
public interface LeaveTaskLogDao extends BaseRepository<LeaveTaskLog, Long> {

}
