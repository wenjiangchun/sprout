package com.sprout.dlyy.devops.host.service;

import com.sprout.core.service.AbstractBaseService;
import com.sprout.dlyy.devops.host.dao.ServerHostDao;
import com.sprout.dlyy.devops.host.entity.ServerHost;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class ServerHostService extends AbstractBaseService<ServerHost, Long> {

    private ServerHostDao serverHostDao;

    public ServerHostService(ServerHostDao serverHostDao) {
        super(serverHostDao);
        this.serverHostDao = serverHostDao;
    }
}


