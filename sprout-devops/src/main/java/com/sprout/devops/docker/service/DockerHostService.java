package com.sprout.devops.docker.service;

import com.sprout.core.jpa.repository.BaseRepository;
import com.sprout.core.service.AbstractBaseService;
import com.sprout.devops.docker.dao.DockerHostDao;
import com.sprout.devops.docker.entity.DockerHost;
import org.springframework.stereotype.Service;

@Service
public class DockerHostService extends AbstractBaseService<DockerHost, Long> {

    private DockerHostDao dockerHostDao;

    public DockerHostService(BaseRepository<DockerHost, Long> dao, DockerHostDao dockerHostDao) {
        super(dao);
        this.dockerHostDao = dockerHostDao;
    }
}
