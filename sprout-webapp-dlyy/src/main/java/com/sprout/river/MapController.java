package com.sprout.river;

import com.sprout.river.entity.Project;
import com.sprout.river.service.ProjectService;
import com.sprout.web.base.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping(value = "/river")
public class MapController extends BaseController {

    private ProjectService projectService;

    public MapController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping(value = "/map")
    public String map() {
        return "/river/map";
    }

    @GetMapping("/getProjectList")
    @ResponseBody
    public List<Project> getProjectList() {
        return projectService.findAll();
    }
}
