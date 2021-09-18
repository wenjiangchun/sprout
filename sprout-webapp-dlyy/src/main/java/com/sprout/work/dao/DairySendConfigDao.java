package com.sprout.work.dao;

import com.sprout.core.jpa.repository.BaseRepository;
import com.sprout.work.entity.DairySendConfig;
import org.springframework.stereotype.Repository;

@Repository
public interface DairySendConfigDao extends BaseRepository<DairySendConfig, Long> {

}
