package com.sprout.data.ds.provider;

import com.sprout.data.ds.entity.DataSourceMeta;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

public interface SproutDataSource {

    Connection getConnection() throws Exception;

    void execute(String sql) throws Exception;

    <T> List<T> query(String sql, Map<Integer, Object> queryParams, Class<T> tClass) throws Exception;

    List<Object> query(String sql, Map<Integer, Object> queryParams) throws Exception;

    boolean getConnectState() throws Exception;

    void closeConnection() throws Exception;

    DataSourceMeta getDataSourceMeta();

    List<Object> getTableNames() throws Exception;
}
