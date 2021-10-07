package com.sprout.oa.leave.dao;

import com.sprout.core.jpa.repository.BaseRepository;
import com.sprout.oa.leave.entity.Leave;
import org.springframework.stereotype.Repository;

@Repository
public interface LeaveDao extends BaseRepository<Leave, Long> {

}
