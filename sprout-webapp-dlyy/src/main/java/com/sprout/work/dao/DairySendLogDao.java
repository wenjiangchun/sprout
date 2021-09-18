package com.sprout.work.dao;

import com.sprout.core.jpa.repository.BaseRepository;
import com.sprout.work.entity.DairySendLog;
import org.springframework.stereotype.Repository;

@Repository
public interface DairySendLogDao extends BaseRepository<DairySendLog, Long> {

}
