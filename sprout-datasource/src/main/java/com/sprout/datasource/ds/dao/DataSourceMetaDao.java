package com.sprout.datasource.ds.dao;

import com.sprout.core.jpa.repository.BaseRepository;
import com.sprout.datasource.ds.entity.DataSourceMeta;
import org.springframework.stereotype.Repository;

@Repository
public interface DataSourceMetaDao extends BaseRepository<DataSourceMeta, Long> {


}
