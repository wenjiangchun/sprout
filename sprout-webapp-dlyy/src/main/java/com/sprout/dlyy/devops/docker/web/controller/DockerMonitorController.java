package com.sprout.dlyy.devops.docker.web.controller;

import com.sprout.dlyy.devops.docker.entity.DockerHost;
import com.sprout.dlyy.devops.docker.service.DockerHostService;
import com.sprout.web.base.BaseCrudController;
import com.sprout.web.util.RestResult;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

@Controller
@RequestMapping(value = "/devops/docker")
public class DockerMonitorController extends BaseCrudController<DockerHost, Long> {

    private DockerHostService dockerHostService;

    private RestTemplate restTemplate;

    public DockerMonitorController(DockerHostService dockerHostService, RestTemplate restTemplate) {
        super("devops", "dockerHost", "Docker", dockerHostService);
        this.dockerHostService = dockerHostService;
        this.restTemplate = restTemplate;
    }

    @Override
    @GetMapping(value = "view")
    public String list(Model model, HttpServletRequest request) {
        setModel(model, request);
        return "devops/docker/list";
    }


    @GetMapping("manager/{id}")
    public String manager(@PathVariable Long id, Model model) {
        DockerHost dockerHost = this.dockerHostService.findById(id);
        model.addAttribute("dockerHost", dockerHost);
        return "devops/docker/manager";
    }

    @GetMapping("manager/getImages")
    public ResponseEntity<String> getImages(DockerHost dockerHost) {
        String remoteURI = "http://" + dockerHost.getIp() + ":" + dockerHost.getPort() + "/";
        return restTemplate.getForEntity(remoteURI + "images/json", String.class);
    }

    @GetMapping("manager/getContainers")
    public ResponseEntity<String> getContainers(DockerHost dockerHost) {
        String remoteURI = "http://" + dockerHost.getIp() + ":" + dockerHost.getPort() + "/";
        return restTemplate.getForEntity(remoteURI + "containers/json?all=true", String.class);
    }

    @PostMapping("deleteContainer/{containerId}")
    @ResponseBody
    public RestResult deleteContainer(DockerHost dockerHost, @PathVariable String containerId) {
        String remoteURI = "http://" + dockerHost.getIp() + ":" + dockerHost.getPort() + "/";
        //restTemplate.delete(remoteURI + "containers/" + containerId + "?force=true");
        return RestResult.createSuccessResult();
    }

    @PostMapping("deleteImage/{imageId}")
    @ResponseBody
    public RestResult deleteImage(DockerHost dockerHost, @PathVariable String imageId) {
        try {
            String remoteURI = "http://" + dockerHost.getIp() + ":" + dockerHost.getPort() + "/";
            restTemplate.delete(remoteURI + "images/" + imageId);
            return RestResult.createSuccessResult();
        } catch (Exception e) {
            return RestResult.createErrorResult(e.getMessage());
        }
    }

    @PostMapping("controlContainer/{containerId}/{command}")
    @ResponseBody
    public RestResult controlContainer(DockerHost dockerHost, @PathVariable String containerId, @PathVariable String command) {
        try {
            String remoteURI = "http://" + dockerHost.getIp() + ":" + dockerHost.getPort() + "/containers/" + containerId + "/" + command;
            ResponseEntity<String> result = restTemplate.postForEntity(remoteURI, null, String.class);
            if (result.getStatusCode() == HttpStatus.OK || result.getStatusCode() == HttpStatus.NO_CONTENT) {
                return RestResult.createSuccessResult(result.getBody());
            } else {
                return RestResult.createErrorResult(result.getBody());
            }
        } catch (Exception e) {
            return RestResult.createErrorResult(e.getMessage());
        }
    }

    @GetMapping("showContainer/{dockerHostId}/{containerId}/{action}")
    public String showContainer(Model model, @PathVariable Long dockerHostId, @PathVariable String containerId, @PathVariable String action) {
        model.addAttribute("dockerHost", this.dockerHostService.findById(dockerHostId));
        model.addAttribute("containerId", containerId);
        model.addAttribute("action", action);
        return "devops/docker/" + action;
    }

    @GetMapping("getContainerInfo/{containerId}/{action}")
    public ResponseEntity<String> getContainerInfo(DockerHost dockerHost, @PathVariable String containerId, @PathVariable String action) {
        String remoteURI = "http://" + dockerHost.getIp() + ":" + dockerHost.getPort() + "/containers/";
        remoteURI += containerId + "/" + action;
        return restTemplate.getForEntity(remoteURI, String.class);
    }

    @GetMapping("getContainerLogs/{containerId}/{action}")
    public ResponseEntity<byte[]> getContainerLogs(DockerHost dockerHost, @PathVariable String containerId, @PathVariable String action) {
        String remoteURI = "http://" + dockerHost.getIp() + ":" + dockerHost.getPort() + "/containers/";
        remoteURI += containerId + "/" + action;
        ResponseEntity<byte[]> out = restTemplate.getForEntity(remoteURI + "?stdout=true", byte[].class);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", containerId + ".log");
        return new ResponseEntity<>(out.getBody(),headers, HttpStatus.OK);
    }

    @PostMapping("renameContainer/{containerId}")
    @ResponseBody
    public RestResult renameContainer(DockerHost dockerHost, @RequestParam String containerName, @PathVariable String containerId) {
        try {
            String remoteURI = "http://" + dockerHost.getIp() + ":" + dockerHost.getPort() + "/containers/";
            remoteURI += containerId + "/rename?name=" + containerName;
            ResponseEntity<String> result = restTemplate.postForEntity(remoteURI, null, String.class);
            if (result.getStatusCode() == HttpStatus.OK || result.getStatusCode() == HttpStatus.NO_CONTENT) {
                return RestResult.createSuccessResult(result.getBody());
            } else {
                return RestResult.createErrorResult(result.getBody());
            }
        } catch (Exception e) {
            return RestResult.createErrorResult(e.getMessage());
        }
    }
}
