package com.sprout.datasource.provider.impala;

import com.sprout.datasource.db.ColumnWrapper;
import com.sprout.datasource.db.TableWrapper;
import com.sprout.datasource.ds.entity.DataSourceMeta;
import com.sprout.datasource.provider.AbstractJdbcSproutDataSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public class ImpalaSproutDataSource extends AbstractJdbcSproutDataSource {

    private boolean usePoll = false;

    public ImpalaSproutDataSource(DataSourceMeta dataSourceMeta) {
        super(dataSourceMeta);
    }

    @Override
    public List<TableWrapper> getTables() throws Exception {
        return null;
    }

    @Override
    public List<ColumnWrapper> getColumnList(String tableName) throws Exception {
        return null;
    }

    @Override
    public Page<Object[]> getDataPage(PageRequest p, String tableName) throws Exception {
        return null;
    }
}
