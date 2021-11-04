package com.sprout.datasource.provider.postgresql;

import com.sprout.datasource.data.db.TableWrapper;
import com.sprout.datasource.data.ds.entity.DataSourceMeta;
import com.sprout.datasource.provider.AbstractJdbcSproutDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostgresqlSproutDataSource extends AbstractJdbcSproutDataSource {

    public PostgresqlSproutDataSource(DataSourceMeta dataSourceMeta) {
        super(dataSourceMeta);
    }

    @Override
    public List<TableWrapper> getTables() throws Exception {
        PreparedStatement preparedStatement = getConnection().prepareStatement("select tablename from pg_tables where schemaname='public'");
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

}
