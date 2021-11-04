package com.sprout.datasource.provider;

import com.sprout.datasource.data.ds.entity.DataSourceMeta;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public abstract class AbstractJdbcSproutDataSource implements SproutDataSource {

    private Connection connection;

    private DataSourceMeta dataSourceMeta;

    public AbstractJdbcSproutDataSource(DataSourceMeta dataSourceMeta) {
        this.dataSourceMeta = dataSourceMeta;
    }

    @Override
    public Connection getConnection() throws Exception{
        if (connection != null && !connection.isClosed()) {
            return connection;
        } else {
            Class.forName(dataSourceMeta.getDataSourceMetaType().getDriverClass());
            connection = DriverManager.getConnection(dataSourceMeta.getUrl(), dataSourceMeta.getUserName(), dataSourceMeta.getPassword());
        }
        return connection;
    }

    @Override
    public void execute(String sql) throws Exception {

    }

    @Override
    public <T> List<T> query(String sql, Map<Integer, Object> queryParams, Class<T> tClass) throws Exception {
        return null;
    }

    @Override
    public List<Object> query(String sql, Map<Integer, Object> queryParams) throws Exception {
        PreparedStatement preparedStatement = getConnection().prepareStatement(sql);
        if (Objects.nonNull(queryParams) && !queryParams.isEmpty()) {
            queryParams.forEach((k, v) -> {
                try {
                    preparedStatement.setObject(k, v);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            });
        }
        ResultSet resultSet = preparedStatement.executeQuery();
        int count = preparedStatement.getMetaData().getColumnCount();
        List<Object> list = new ArrayList<>();
        while (resultSet.next()) {
            List<Object> row = new ArrayList<>();
            for (int i = 1; i <= count; i++) {
                row.add(resultSet.getObject(i));
            }
            list.add(row);
        }
        resultSet.close();
        preparedStatement.close();
        return list;
    }

    @Override
    public boolean getConnectState() throws Exception {
        return getConnection().isClosed();
    }

    @Override
    public void closeConnection() throws Exception {
        getConnection().close();
    }

    @Override
    public DataSourceMeta getDataSourceMeta() {
        return this.dataSourceMeta;
    }



}
