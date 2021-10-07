package com.sprout.flowable.service;

import com.sprout.core.spring.SpringContextUtils;
import org.flowable.engine.FormService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.Model;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ModelService {

    private RepositoryService repositoryService;

    public ModelService(RepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }

    public List<Model> getModelList() {
        return this.repositoryService.createModelQuery().list();
    }
}
