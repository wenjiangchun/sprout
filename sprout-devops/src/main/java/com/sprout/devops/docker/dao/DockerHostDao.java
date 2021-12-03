package com.sprout.devops.docker.dao;

import com.sprout.core.jpa.repository.BaseRepository;
import com.sprout.devops.docker.entity.DockerHost;
import org.springframework.stereotype.Repository;

@Repository
public interface DockerHostDao extends BaseRepository<DockerHost, Long> {

}
