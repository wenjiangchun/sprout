package com.sprout.oa.asset.dao;

import com.sprout.core.jpa.repository.BaseRepository;
import com.sprout.oa.asset.entity.AssetOutLog;
import org.springframework.stereotype.Repository;

@Repository
public interface AssetOutLogDao extends BaseRepository<AssetOutLog, Long> {

}
