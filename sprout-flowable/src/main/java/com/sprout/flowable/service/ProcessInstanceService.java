package com.sprout.flowable.service;

import org.flowable.bpmn.model.BpmnModel;
import org.flowable.engine.*;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.impl.RepositoryServiceImpl;
import org.flowable.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.image.ProcessDiagramGenerator;
import org.flowable.task.api.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class ProcessInstanceService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private RuntimeService runtimeService;

    private TaskService taskService;

    private RepositoryService repositoryService;

    private HistoryService historyService;

    public ProcessInstanceService(RepositoryService repositoryService, RuntimeService runtimeService, TaskService taskService, HistoryService historyService) {
        this.repositoryService = repositoryService;
        this.runtimeService = runtimeService;
        this.taskService = taskService;
        this.historyService = historyService;
    }

    /**
     * 获取运行中流程实例
     * @return 流程实例列表
     */
    public List<ProcessInstance> getRuntimeProcessInstance() {
        return runtimeService.createProcessInstanceQuery().list();
    }

    /**
     * 激活流程实例信息
     * @param processInstanceId 流程实例Id
     */
    public void activateProcessInstanceById(String processInstanceId) {
        runtimeService.activateProcessInstanceById(processInstanceId);
        logger.debug("已激活流程信息，流程id=【{}】", processInstanceId);
    }

    /**
     * 挂起流程实例信息
     * @param processInstanceId 流程实例Id
     */
    public void suspendProcessInstanceById(String processInstanceId) {
        runtimeService.suspendProcessInstanceById(processInstanceId);
        logger.debug("已挂起流程信息，流程id=【{}】", processInstanceId);
    }

    /**
     * 根据流程实例ID查找流程对象信息
     * @param processInstanceId 流程实例ID
     * @return 流程实例对象信息
     */
    public ProcessInstance getProcessInstanceById(String processInstanceId) {
        return runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
    }

    /**
     * 根据发起人ID和流程定义Key查询流程信息
     * @param startUserId 流程发起人ID
     * @param processDefinitionKey 流程定义key
     * @return 运行中的流程实例列表
     */
    public List<ProcessInstance> getProcessInstanceList(String startUserId, String processDefinitionKey) {
        return runtimeService.createProcessInstanceQuery().startedBy(startUserId).processDefinitionKey(processDefinitionKey).list();
    }

    /**
     * 根据流程实例ID获取任务列表
     * @param processInstanceId 流程实例ID
     * @return 任务列表
     */
    public List<Task> getTaskList(String processInstanceId) {
        return this.taskService.createTaskQuery().processDefinitionId(processInstanceId).list();
    }

    public List<HistoricActivityInstance> getHistoryProcess(String processInstanceId) {
        return this.historyService.createHistoricActivityInstanceQuery().processInstanceId(processInstanceId).finished().list();
    }

    public InputStream getProcessDiagram(String processInstanceId) {

        ProcessInstance pi = this.getProcessInstanceById(processInstanceId);
        //流程走完的不显示图
        if (pi == null) {
            return null;
        }
        List<HistoricActivityInstance> historyProcess = this.getHistoryProcess(processInstanceId);
        List<String> activityIds = new ArrayList<>();
        List<String> flows = new ArrayList<>();
        //获取流程图
        BpmnModel bpmnModel = repositoryService.getBpmnModel(pi.getProcessDefinitionId());
        for (HistoricActivityInstance hi : historyProcess) {
            String activityType = hi.getActivityType();
            if (activityType.equals("sequenceFlow") || activityType.equals("exclusiveGateway")) {
                flows.add(hi.getActivityId());
            } else if (activityType.equals("userTask") || activityType.equals("startEvent")) {
                activityIds.add(hi.getActivityId());
            }
        }
        List<Task> tasks = this.getTaskList(processInstanceId);
        for (Task task : tasks) {
            activityIds.add(task.getTaskDefinitionKey());
        }
        ProcessEngineConfiguration engConf = ProcessEngines.getDefaultProcessEngine().getProcessEngineConfiguration();
        //定义流程画布生成器
        ProcessDiagramGenerator processDiagramGenerator = engConf.getProcessDiagramGenerator();
        return processDiagramGenerator.generateDiagram(bpmnModel, "png", activityIds, flows, engConf.getActivityFontName(), engConf.getLabelFontName(), engConf.getAnnotationFontName(), engConf.getClassLoader(), 1.0, true);
    }
}
