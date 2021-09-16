package com.sprout.river.dao;

import com.sprout.core.jpa.repository.BaseRepository;
import com.sprout.river.entity.ProjectFile;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectFileDao extends BaseRepository<ProjectFile, Long> {

}
