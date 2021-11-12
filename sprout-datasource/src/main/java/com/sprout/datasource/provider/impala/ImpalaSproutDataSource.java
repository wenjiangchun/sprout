package com.sprout.datasource.provider.impala;

import com.sprout.datasource.db.ColumnWrapper;
import com.sprout.datasource.db.TableWrapper;
import com.sprout.datasource.ds.entity.DataSourceMeta;
import com.sprout.datasource.provider.AbstractJdbcSproutDataSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ImpalaSproutDataSource extends AbstractJdbcSproutDataSource {

    private boolean usePoll = false;

    public ImpalaSproutDataSource(DataSourceMeta dataSourceMeta) {
        super(dataSourceMeta);
    }

    @Override
    public List<TableWrapper> getTables() throws Exception {
        PreparedStatement preparedStatement = getConnection().prepareStatement("show tables");
        ResultSet resultSet = preparedStatement.executeQuery();
        int count = preparedStatement.getMetaData().getColumnCount();
        List<TableWrapper> list = new ArrayList<>();
        while (resultSet.next()) {
            TableWrapper tableWrapper = new TableWrapper();
            for (int i = 1; i <= count; i++) {
                tableWrapper.setTableName(resultSet.getObject(i).toString());
            }
            list.add(tableWrapper);
        }
        resultSet.close();
        preparedStatement.close();
        return list;
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
