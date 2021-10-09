package com.sprout.flowable.web.controller;

import com.sprout.flowable.service.ProcessDefinitionService;
import com.sprout.web.util.RestResult;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/flowable/processDefinition")
public class ProcessDefinitionController {

    private ProcessDefinitionService processDefinitionService;

    public ProcessDefinitionController(ProcessDefinitionService processDefinitionService) {
        this.processDefinitionService = processDefinitionService;
    }

    @GetMapping(value = "view")
    public String list(Model model) {
        List<Object[]> processList = new ArrayList<>();
        List<ProcessDefinition> processDefinitionList = processDefinitionService
                .getProcessDefinitionList();
        for (ProcessDefinition processDefinition : processDefinitionList) {
            Deployment deployment = processDefinitionService
                    .getDeploymentByProcessDefinition(processDefinition);
            processList.add(new Object[]{processDefinition, deployment});
        }
        model.addAttribute("processDefinitionList", processList);
        return "flowable/processDefinition/list";
    }

    /**
     * 删除流程定义信息
     */
    @PostMapping(value = "deleteProcessDefination/{deploymentId}")
    @ResponseBody
    public RestResult deleteProcessDefination(@PathVariable("deploymentId") String deploymentId) {
        processDefinitionService.deleteDeployment(deploymentId, true);
        return RestResult.createSuccessResult();
    }

    /**
     * 更改流程定义信息状态
     */
    @PostMapping(value = "updateProcessDefinitionState/{processInstanceId}/{state}")
    @ResponseBody
    public RestResult updateProcessDefinitionState(@PathVariable("processInstanceId") String processInstanceId, @PathVariable("state") String state) {
        if (state.equals("active")) {
            processDefinitionService.activateProcessDefinitionById(processInstanceId, true);
            return RestResult.createSuccessResult("已激活ID为[" + processInstanceId + "]的流程定义。");
        } else if (state.equals("suspend")) {
            processDefinitionService.suspendProcessDefinitionById(processInstanceId, true);
            return RestResult.createSuccessResult("已挂起ID为[" + processInstanceId + "]的流程定义。");
        }
        return RestResult.createErrorResult("未知状态标识符");
    }

    /**
     * 根据部署Id和资源名称获取流程资源信息 其中资源名称可以是流程图片名称或流程定义Xml文件名称
     * @param deploymentId 部署ID
     * @param resourceName 资源名称
     * @param response 响应流
     * @throws Exception 获取失败抛出该异常
     */
    @GetMapping(value = "/getProcessResource")
    public void getProcessResource(@RequestParam("deploymentId") String deploymentId, @RequestParam("resourceName") String resourceName, HttpServletResponse response) throws Exception {
        InputStream resourceAsStream = processDefinitionService.getProcessResource(deploymentId, resourceName);
        byte[] b = new byte[1024];
        int len = -1;
        while ((len = resourceAsStream.read(b, 0, 1024)) != -1) {
            response.getOutputStream().write(b, 0, len);
        }
    }

    @PostMapping(value = "/deployProcessDefinition")
    @ResponseBody
    public RestResult deployProcessDefinition(@Value("/home/") String exportDir, @RequestParam(value = "file", required = false) MultipartFile file) {
        try {
            processDefinitionService.deployProcessDefinition(file, exportDir);
            return RestResult.createSuccessResult();
        } catch (Exception e) {
            e.printStackTrace();
            return RestResult.createErrorResult(e.getMessage());
        }
    }
}
