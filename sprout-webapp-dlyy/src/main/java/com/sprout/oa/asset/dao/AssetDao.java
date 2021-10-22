package com.sprout.oa.asset.dao;

import com.sprout.core.jpa.repository.BaseRepository;
import com.sprout.oa.asset.entity.Asset;
import org.springframework.stereotype.Repository;

@Repository
public interface AssetDao extends BaseRepository<Asset, Long> {

}
