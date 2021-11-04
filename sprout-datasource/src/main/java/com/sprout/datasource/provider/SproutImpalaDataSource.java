package com.sprout.datasource.provider;

import com.sprout.datasource.data.ds.entity.DataSourceMeta;

public abstract class SproutImpalaDataSource extends AbstractJdbcSproutDataSource {

    private boolean usePoll = false;

    public SproutImpalaDataSource(DataSourceMeta dataSourceMeta) {
        super(dataSourceMeta);
    }
}
