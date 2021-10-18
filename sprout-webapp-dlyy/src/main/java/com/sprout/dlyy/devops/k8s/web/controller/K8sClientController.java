package com.sprout.dlyy.devops.k8s.web.controller;

import com.sprout.dlyy.devops.k8s.K8sUtils;
import com.sprout.web.base.BaseController;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.models.V1Node;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/devops/k8s")
public class K8sClientController extends BaseController {

    @GetMapping("view")
    public String view(Model model) {
        try {
            //List<V1Pod> podList = K8sUtils.getPods();
            List<V1Node> nodeList = K8sUtils.getNodes();
           // model.addAttribute("podList",podList);
            model.addAttribute("nodeList", nodeList);
        } catch (Exception e) {
           // model.addAttribute("podList",new ArrayList<>());
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("nodeList",new ArrayList<>());
            e.printStackTrace();
        }
        return "devops/k8s/nodeList";
    }

    @GetMapping("getNodeList")
    @ResponseBody
    public List<V1Node> getNodeList() throws Exception {
        return K8sUtils.getNodes();
    }

}
