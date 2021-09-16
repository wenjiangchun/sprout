package com.sprout.river.service;

import com.sprout.common.Digests;
import com.sprout.common.util.EncodeUtils;
import com.sprout.common.util.SproutStringUtils;
import com.sprout.core.service.AbstractBaseService;
import com.sprout.core.spring.SpringContextUtils;
import com.sprout.river.dao.ProjectDao;
import com.sprout.river.dao.ProjectFileDao;
import com.sprout.river.entity.Project;
import com.sprout.system.dao.UserDao;
import com.sprout.system.entity.Group;
import com.sprout.system.entity.Role;
import com.sprout.system.entity.User;
import com.sprout.system.event.UserChangeGroupEvent;
import com.sprout.system.event.UserRoleChangeEvent;
import com.sprout.system.exception.UserLoginNameExistException;
import com.sprout.system.utils.Status;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


/**
 * 用户业务操作类
 *
 * @author sofar
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ProjectService extends AbstractBaseService<Project, Long> {


    private ProjectDao projectDao;

    private ProjectFileDao projectFileDao;

    public ProjectService(ProjectDao projectDao, ProjectFileDao projectFileDao) {
        super(projectDao);
        this.projectDao = projectDao;
        this.projectFileDao = projectFileDao;
    }


}
