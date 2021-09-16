package com.sprout.river.web.controller;

import com.sprout.river.config.RiverConfig;
import com.sprout.river.entity.Project;
import com.sprout.river.entity.ProjectFile;
import com.sprout.river.service.ProjectService;
import com.sprout.web.base.BaseCrudController;
import com.sprout.web.util.RestResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Controller
@RequestMapping(value = "/river/project")
public class ProjectController extends BaseCrudController<Project, Long> {

    private ProjectService projectService;

    private RiverConfig riverConfig;

    public ProjectController(ProjectService projectService, RiverConfig riverConfig) {
        super("river", "project", "项目信息", projectService);
        this.projectService = projectService;
        this.riverConfig = riverConfig;
    }

    @Override
    protected void setPageQueryVariables(Map<String, Object> queryVariables, HttpServletRequest request) {

    }

    @Override
    protected void setModel(Model model, HttpServletRequest request) {

    }

    @GetMapping("/addProject")
    public String addProject(Model model, @RequestParam String x, @RequestParam String y) {
        model.addAttribute("x", x);
        model.addAttribute("y",y);
        return "/river/project/add";
    }
    @PostMapping("/saveProject")
    @ResponseBody
    public Project saveProject(Model model, Project project, MultipartHttpServletRequest request) {
        try {
            String path = riverConfig.getPath();
            List<MultipartFile> fileList =  request.getFiles("file");
            Set<ProjectFile> projectFileList = new HashSet<>();
            for (MultipartFile multipartFile : fileList) {
                Path destinationPath = Paths.get(path + FileSystems.getDefault().getSeparator() + multipartFile.getOriginalFilename());
                multipartFile.transferTo(destinationPath);
                ProjectFile projectFile = new ProjectFile();
                projectFile.setPath(destinationPath.toString());
                projectFile.setName(multipartFile.getOriginalFilename());
                projectFile.setSize(multipartFile.getSize());
                projectFileList.add(projectFile);
            }
            project.setProjectFiles(projectFileList);
            return projectService.save(project);
        } catch (Exception e) {
            return null;
        }
    }

    @GetMapping("/showProject")
    public String showProject(Model model, @RequestParam Long id) {
        model.addAttribute("project", projectService.findById(id));
        return "/river/project/info";
    }

    @PostMapping("/deleteProject")
    @ResponseBody
    public RestResult deleteProject(@RequestParam Long id) {
        try {
            Project project = projectService.findById(id);
            project.setProjectFiles(null);
            projectService.delete(project);
            return RestResult.createSuccessResult(project, "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return RestResult.createErrorResult("删除失败，请联系管理员！");
        }
    }
}
