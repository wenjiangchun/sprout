package com.sprout.oa.leave.dao;

import com.sprout.core.jpa.repository.BaseRepository;
import com.sprout.oa.leave.entity.LeaveStatistic;
import com.sprout.system.entity.Dict;
import com.sprout.system.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaveStatisticDao extends BaseRepository<LeaveStatistic, Long> {

    List<LeaveStatistic> getLeaveStatisticByApplierIdAndYearAndMonth(Long applierId, int year, int month);

    LeaveStatistic getLeaveStatisticByApplierAndLeaveTypeAndYearAndMonth(User applier, Dict leaveType, int year, int month);

}
