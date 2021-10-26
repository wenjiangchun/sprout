package com.sprout.oa.leave.service;

import com.sprout.core.service.AbstractBaseService;
import com.sprout.core.spring.SpringContextUtils;
import com.sprout.flowable.service.ProcessInstanceService;
import com.sprout.oa.leave.dao.LeaveDao;
import com.sprout.oa.leave.dao.LeaveStatisticDao;
import com.sprout.oa.leave.dao.LeaveTaskLogDao;
import com.sprout.oa.leave.entity.Leave;
import com.sprout.oa.leave.entity.LeaveStatistic;
import com.sprout.oa.leave.entity.LeaveTaskLog;
import com.sprout.oa.notice.NoticeMessage;
import com.sprout.oa.notice.NoticeType;
import com.sprout.system.entity.Dict;
import com.sprout.system.service.DictService;
import com.sprout.web.websocket.WebSocketServer;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class LeaveStatisticService extends AbstractBaseService<LeaveStatistic, Long> {

    private LeaveStatisticDao leaveStatisticDao;

    public LeaveStatisticService(LeaveStatisticDao leaveStatisticDao) {
        super(leaveStatisticDao);
        this.leaveStatisticDao = leaveStatisticDao;
    }

}
