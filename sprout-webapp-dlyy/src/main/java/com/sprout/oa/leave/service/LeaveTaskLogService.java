package com.sprout.oa.leave.service;

import com.sprout.core.service.AbstractBaseService;
import com.sprout.oa.leave.dao.LeaveTaskLogDao;
import com.sprout.oa.leave.entity.Leave;
import com.sprout.oa.leave.entity.LeaveTaskLog;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeaveTaskLogService extends AbstractBaseService<LeaveTaskLog, Long> {

    private LeaveTaskLogDao leaveTaskLogDao;

    public LeaveTaskLogService(LeaveTaskLogDao leaveTaskLogDao) {
        super(leaveTaskLogDao);
        this.leaveTaskLogDao = leaveTaskLogDao;
    }

    public List<LeaveTaskLog> findByLeaveId(Long leaveId) {
        Leave leave = new Leave();
        leave.setId(leaveId);
        return this.findByProperty("leave", leave, Sort.by(Sort.Direction.ASC, "handleTime"));
    }
}
