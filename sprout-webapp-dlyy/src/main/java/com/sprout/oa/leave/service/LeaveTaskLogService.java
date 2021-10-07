package com.sprout.oa.leave.service;

import com.sprout.core.service.AbstractBaseService;
import com.sprout.oa.leave.dao.LeaveTaskLogDao;
import com.sprout.oa.leave.entity.LeaveTaskLog;
import org.springframework.stereotype.Service;

@Service
public class LeaveTaskLogService extends AbstractBaseService<LeaveTaskLog, Long> {

    private LeaveTaskLogDao leaveTaskLogDao;

    public LeaveTaskLogService(LeaveTaskLogDao leaveTaskLogDao) {
        super(leaveTaskLogDao);
        this.leaveTaskLogDao = leaveTaskLogDao;
    }
}
