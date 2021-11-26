package com.sprout.dlyy.devops.host.web.controller;

import com.jcraft.jsch.JSchException;
import com.sprout.dlyy.devops.host.entity.ServerHost;
import com.sprout.dlyy.devops.host.service.ServerHostService;
import com.sprout.dlyy.devops.util.OSType;
import com.sprout.dlyy.devops.util.SSHClient;
import com.sprout.web.base.BaseCrudController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;

@Controller
@RequestMapping(value = "/devops/serverHost")
public class ServerHostController extends BaseCrudController<ServerHost, Long> {

    private ServerHostService serverHostService;

    private RestTemplate restTemplate;

    public ServerHostController(ServerHostService serverHostService) {
        super("devops", "serverHost", "ServerHost", serverHostService);
        this.serverHostService = serverHostService;
    }

    @Override
    @GetMapping(value = "view")
    public String list(Model model, HttpServletRequest request) {
        setModel(model, request);
        return "devops/serverHost/list";
    }

    @Override
    protected void setModel(Model model, HttpServletRequest request) {
        super.setModel(model, request);
        model.addAttribute("osTypeList", OSType.values());
    }

    @GetMapping("checkServerState/{ip}")
    @ResponseBody
    public boolean checkServerState(@PathVariable String ip) {
        try {
            InetAddress inetAddress =  InetAddress.getByName(ip);
            return inetAddress.isReachable(2000);
        } catch (Exception e) {
            return false;
        }
    }

    @GetMapping("manager/{id}")
    public String manager(@PathVariable Long id, Model model) {
        ServerHost serverHost = this.serverHostService.findById(id);
        model.addAttribute("serverHost", serverHost);
        return "devops/serverHost/manager";
    }

    @GetMapping("runCMD/{id}")
    @ResponseBody
    public String runCMD(@PathVariable Long id, @RequestParam String cmd) {
        ServerHost serverHost = this.serverHostService.findById(id);
        try {
            SSHClient sshClient = new SSHClient(serverHost);
            String result = sshClient.execCmd(cmd);
            sshClient.close();
            return result;
        } catch (JSchException e) {
            return "获取服务器信息失败：" + e.getMessage();
        }
    }

    @GetMapping("execServer/{id}")
    @ResponseBody
    public String execServer(@PathVariable Long id) {
        try {
            ServerHost serverHost = this.serverHostService.findById(id);
            if (checkServerState(serverHost.getIp())) {
                //关闭
                return runCMD(id,"shutdown -h now");
            } else {
                //启动 TODO
                return null;
            }
        } catch (Exception e) {
            return "执行失败：" + e.getMessage();
        }
    }
}
