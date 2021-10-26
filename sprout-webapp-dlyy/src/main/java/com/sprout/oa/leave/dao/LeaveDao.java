package com.sprout.oa.leave.dao;

import com.sprout.core.jpa.repository.BaseRepository;
import com.sprout.oa.leave.entity.Leave;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface LeaveDao extends BaseRepository<Leave, Long> {

    @Query("from Leave lv where lv.applier.id=:userId and lv.state=2 and lv.applyTime>=:startDay and lv.applyTime <=:endDay order by lv.applyTime desc")
    List<Leave> findLeaveListByApplierId(Long userId, Date startDay, Date endDay);
}
