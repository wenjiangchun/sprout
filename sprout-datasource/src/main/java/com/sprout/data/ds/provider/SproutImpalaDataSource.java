package com.sprout.data.ds.provider;

import com.sprout.data.ds.entity.DataSourceMeta;

public class SproutImpalaDataSource extends JdbcSproutDataSource{

    private boolean usePoll = false;

    public SproutImpalaDataSource(DataSourceMeta dataSourceMeta) {
        super(dataSourceMeta);
    }
}
