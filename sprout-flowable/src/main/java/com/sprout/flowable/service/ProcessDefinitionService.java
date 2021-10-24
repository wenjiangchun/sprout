package com.sprout.flowable.service;

import com.sprout.flowable.util.FlowableUtils;
import org.apache.commons.io.FilenameUtils;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipInputStream;

@Service
@Transactional(readOnly = true)
public class ProcessDefinitionService {

    private Logger logger = LoggerFactory.getLogger(ProcessDefinitionService.class);

    private RepositoryService repositoryService;

    public ProcessDefinitionService(RepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }

    /**
     * 获取流程定义信息
     * @return 流程定义集合
     */
    public List<ProcessDefinition> getProcessDefinitionList() {
        return repositoryService.createProcessDefinitionQuery().list();
    }

    /**
     * 获取激活状态的流程定义信息
     * @return 激活状态的流程定义集合
     */
    public List<ProcessDefinition> getActiveProcessDefinitionList() {
        return repositoryService.createProcessDefinitionQuery().active().list();
    }

    /**
     * 获取流程定义部署信息
     * @param processDefinition 流程定义
     * @return 流程部署信息
     */
    public Deployment getDeploymentByProcessDefinition(ProcessDefinition processDefinition) {
        String deploymentId = processDefinition.getDeploymentId();
        Deployment deployment = repositoryService.createDeploymentQuery().deploymentId(deploymentId).singleResult();
        return deployment;
    }

    /**
     * 部署流程定义信息
     * @param file 流程文件
     * @param exportDir 导出路径
     */
    @Transactional
    public void deployProcessDefinition(MultipartFile file, String exportDir) throws Exception {
        String fileName = file.getOriginalFilename();
        try {
            String extension = FilenameUtils.getExtension(fileName);
            //File destination = new File("e:\\" + System.currentTimeMillis() + extension);
            //file.transferTo(destination);
            //InputStream fileInputStream = new FileInputStream(destination);
            InputStream fileInputStream = file.getInputStream();
            Deployment deployment = null;
            if (extension.equals("zip") || extension.equals("bar")) {
                ZipInputStream zip = new ZipInputStream(fileInputStream);
                deployment = repositoryService.createDeployment().addZipInputStream(zip).deploy();
            } else {
                deployment = repositoryService.createDeployment().addInputStream(fileName, fileInputStream).deploy();
            }
            List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).list();
            for (ProcessDefinition processDefinition : list) {
                FlowableUtils.exportDiagramToFile(repositoryService, processDefinition, exportDir);
            }
        } catch (Exception e) {
            logger.error("error on deploy process, because of file input stream", e);
            throw new Exception("部署失败");
        }
    }

    /**
     * 删除流程部署信息
     * @param deploymentId 流程部署Id
     * @param b 是否删除关联信息
     */
    @Transactional
    public void deleteDeployment(String deploymentId, boolean b) {
        repositoryService.deleteDeployment(deploymentId, b);
    }

    /**
     * 根据流程定义id和状态激活流程信息
     * @param processDefinitionId 流程定义ID
     * @param activateProcessInstances 流程定义状态
     */
    @Transactional
    public void activateProcessDefinitionById(String processDefinitionId, boolean activateProcessInstances) {
        repositoryService.activateProcessDefinitionById(processDefinitionId, activateProcessInstances, null);
    }

    /**
     * 根据流程定义id和状态挂起流程信息
     * <p>当挂起流程定义信息后，该流程定义下所有流程实例同时状态改变为挂起</p>
     * @param processDefinitionId 流程定义ID
     * @param suspendProcessInstances 流程定义状态
     */
    @Transactional
    public void suspendProcessDefinitionById(String processDefinitionId,
                                             boolean suspendProcessInstances) {
        repositoryService.suspendProcessDefinitionById(processDefinitionId, suspendProcessInstances, null);
    }

    /**
     * 根据流程部署Id和资源名称获取资源信息
     * @param deploymentId 流程部署Id
     * @param resourceName 资源名称 可以为Xml文件名和流程图片名称
     * @return InputStream
     */
    public InputStream getProcessResource(String deploymentId, String resourceName) {
        return repositoryService.getResourceAsStream(deploymentId, resourceName);
    }

}
