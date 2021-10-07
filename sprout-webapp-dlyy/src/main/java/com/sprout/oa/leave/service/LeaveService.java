package com.sprout.oa.leave.service;

import com.sprout.core.service.AbstractBaseService;
import com.sprout.oa.leave.dao.LeaveDao;
import com.sprout.oa.leave.entity.Leave;
import com.sprout.system.entity.Dict;
import com.sprout.system.service.DictService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class LeaveService extends AbstractBaseService<Leave, Long> {

    private static final String DICT_LEAVE_TYPE = "LEAVE_TYPE";

    private static final String PROCESS_KEY = "leaveProcess";

    private LeaveDao leaveDao;

    private RuntimeService runtimeService;

    public LeaveService(LeaveDao leaveDao, RuntimeService runtimeService) {
        super(leaveDao);
        this.leaveDao = leaveDao;
        this.runtimeService = runtimeService;
    }

    @Transactional(readOnly = true)
    public List<Dict> getLeaveTypeList(DictService dictService) {
        return dictService.findChildsByRootCode(DICT_LEAVE_TYPE);
    }

    public ProcessInstance startWorkflow(Leave leave, Map<String, Object> variables) throws Exception {
        this.save(leave);
        String businessKey = leave.getId().toString();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("leaveProcess1", businessKey, variables);
        String processInstanceId = processInstance.getId();
        leave.setProcessInstanceId(processInstanceId);
        leave.setApplyTime(processInstance.getStartTime());
        //this.save(leave);
        logger.debug("start process of {key={}, bkey={}, pid={}, variables={}}", "leave", businessKey, processInstanceId, variables);
        return processInstance;
    }
}
