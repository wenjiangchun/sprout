package com.sprout.oa.asset.dao;

import com.sprout.core.jpa.repository.BaseRepository;
import com.sprout.oa.asset.entity.AssetOut;
import org.springframework.stereotype.Repository;

@Repository
public interface AssetOutDao extends BaseRepository<AssetOut, Long> {

}
