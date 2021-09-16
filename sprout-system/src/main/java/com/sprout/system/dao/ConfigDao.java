package com.sprout.system.dao;

import com.sprout.core.jpa.repository.BaseRepository;
import com.sprout.system.entity.Config;
import org.springframework.stereotype.Repository;

/**
 * 系统配置Dao接口定义类
 *
 * @author sofar
 *
 */
@Repository
public interface ConfigDao extends BaseRepository<Config, Long> {

}
