package com.sprout.flowable.web.controller;

import com.sprout.flowable.service.ModelService;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/flowable/model")
public class RepositoryModelController {
    private ModelService modelService;

    public RepositoryModelController(ModelService modelService) {
        this.modelService = modelService;
    }

    @GetMapping(value = "view")
    public String list(Model model) {
        model.addAttribute("modelList", this.modelService.getModelList());
        return "flowable/model/list";
    }
}
