package com.sprout.work.dao;

import com.sprout.core.jpa.repository.BaseRepository;
import com.sprout.work.entity.WorkDairy;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkDairyDao extends BaseRepository<WorkDairy, Long> {

}
