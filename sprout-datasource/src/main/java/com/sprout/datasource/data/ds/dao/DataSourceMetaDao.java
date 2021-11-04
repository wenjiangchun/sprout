package com.sprout.datasource.data.ds.dao;

import com.sprout.core.jpa.repository.BaseRepository;
import com.sprout.datasource.data.ds.entity.DataSourceMeta;
import org.springframework.stereotype.Repository;

@Repository
public interface DataSourceMetaDao extends BaseRepository<DataSourceMeta, Long> {


}
