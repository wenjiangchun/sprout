package com.sprout.dlyy.devops.host.dao;

import com.sprout.core.jpa.repository.BaseRepository;
import com.sprout.dlyy.devops.host.entity.ServerHost;
import org.springframework.stereotype.Repository;

@Repository
public interface ServerHostDao extends BaseRepository<ServerHost, Long> {

}
