package com.sprout.dlyy.k8s;

import com.sprout.dlyy.kudu.KuduTableMeta;
import com.sprout.dlyy.kudu.KuduUtils;
import com.sprout.web.base.BaseController;
import io.kubernetes.client.openapi.models.V1Node;
import io.kubernetes.client.openapi.models.V1NodeAddress;
import io.kubernetes.client.openapi.models.V1Pod;
import org.apache.kudu.Schema;
import org.apache.kudu.client.KuduClient;
import org.apache.kudu.client.KuduException;
import org.apache.kudu.client.KuduTable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/dlyy/k8s")
public class K8sClientController extends BaseController {

    @GetMapping("view")
    public String view(Model model) {
        try {
            List<V1Pod> podList = K8sUtils.getPods();
            List<V1Node> nodeList = K8sUtils.getNodes();
            model.addAttribute("podList",podList);
            model.addAttribute("nodeList",K8sNode.getK8sNodeList(nodeList));
        } catch (Exception e) {
            model.addAttribute("podList",new ArrayList<>());
            model.addAttribute("po",new ArrayList<>());
            e.printStackTrace();
        }
        return "dlyy/k8s/k8s";
    }

}
