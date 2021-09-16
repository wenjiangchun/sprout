package com.sprout.river.dao;

import com.sprout.core.jpa.repository.BaseRepository;
import com.sprout.river.entity.Project;
import com.sprout.system.entity.User;
import com.sprout.system.utils.Status;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectDao extends BaseRepository<Project, Long> {

}
