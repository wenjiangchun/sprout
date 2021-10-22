package com.sprout.oa.asset.dao;

import com.sprout.core.jpa.repository.BaseRepository;
import com.sprout.oa.asset.entity.AssetIn;
import org.springframework.stereotype.Repository;

@Repository
public interface AssetInDao extends BaseRepository<AssetIn, Long> {

}
