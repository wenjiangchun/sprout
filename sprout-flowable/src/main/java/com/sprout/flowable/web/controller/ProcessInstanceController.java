package com.sprout.flowable.web.controller;

import com.sprout.flowable.service.ProcessInstanceService;
import com.sprout.web.util.RestResult;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

@Controller
@RequestMapping(value = "/flowable/processInstance")
public class ProcessInstanceController {

    private ProcessInstanceService processInstanceService;

    public ProcessInstanceController(ProcessInstanceService processInstanceService) {
        this.processInstanceService = processInstanceService;
    }

    @GetMapping(value = "view")
    public String list(Model model) {
        List<ProcessInstance> processInstanceList = processInstanceService.getRuntimeProcessInstance();
        model.addAttribute("processInstanceList", processInstanceList);
        return "flowable/processInstance/list";
    }

    /**
     * 更改流程定义信息状态
     */
    @PostMapping(value = "updateProcessInstanceState/{processInstanceId}/{state}")
    @ResponseBody
    public RestResult updateProcessInstanceState(@PathVariable("processInstanceId") String processInstanceId, @PathVariable("state") String state) {
        if (state.equals("active")) {
            processInstanceService.activateProcessInstanceById(processInstanceId);
            return RestResult.createSuccessResult("已激活ID为[" + processInstanceId + "]的流程实例。");
        } else if (state.equals("suspend")) {
            processInstanceService.suspendProcessInstanceById(
                    processInstanceId);
            return RestResult.createSuccessResult("已挂起ID为[" + processInstanceId + "]的流程实例。");
        }
        return RestResult.createErrorResult("未知状态标识符");
    }

    @GetMapping(value = "genProcessDiagram/{processInstanceId}")
    public void genProcessDiagram(HttpServletResponse httpServletResponse, @PathVariable String processInstanceId) throws Exception {
        byte[] buf = new byte[1024];
        int length;
        try (InputStream in = this.processInstanceService.getProcessDiagram(processInstanceId); OutputStream out = httpServletResponse.getOutputStream()) {
            while ((length = in.read(buf)) != -1) {
                out.write(buf, 0, length);
            }
        }
    }
}
