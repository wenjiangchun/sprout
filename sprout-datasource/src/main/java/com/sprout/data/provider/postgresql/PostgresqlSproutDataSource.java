package com.sprout.data.provider.postgresql;

import com.sprout.data.db.TableWrapper;
import com.sprout.data.ds.entity.DataSourceMeta;
import com.sprout.data.provider.AbstractJdbcSproutDataSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    /*@Override
    public List<ColumnWrapper> getColumnList(String tableName) throws Exception {
        String sql = "SELECT " +
                "       c.relname                                          AS tablename,\n" +
                "       a.attname                                          AS columnname,\n" +
                "       col_description(a.attrelid, a.attnum)              AS comment,\n" +
                "       format_type(a.atttypid, a.atttypmod)               AS columnType,\n" +
                "       a.attnotnull                                       AS attIsNull,\n" +
                "       a.attnum                                           As   num\n" +
                "FROM pg_class AS c,\n" +
                "     pg_attribute AS a\n" +
                "WHERE c.relname =?\n" +
                "  AND a.attrelid = C.oid\n" +
                "  AND a.attnum > 0\n" +
                "ORDER BY c.relname, a.attnum";
        PreparedStatement preparedStatement = getConnection().prepareStatement(sql);
        preparedStatement.setString(1, tableName);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<ColumnWrapper> list = new ArrayList<>();
        while (resultSet.next()) {
            ColumnWrapper columnWrapper = new ColumnWrapper();
            columnWrapper.setColumnName(resultSet.getString(2));
            columnWrapper.setColumnType(resultSet.getString(4));
            columnWrapper.setComment(resultSet.getString(3));
            columnWrapper.setNum(resultSet.getInt(6));
            columnWrapper.setNotNull(!resultSet.getBoolean(5));
            list.add(columnWrapper);
        }
        resultSet.close();
        preparedStatement.close();
        return list;
        //return null;
    }*/

    @Override
    public List<Map<String, Object>> getColumns(String tableName) throws Exception {
        String sql = "SELECT " +
                "       a.attname                                          AS name,\n" +
                "       col_description(a.attrelid, a.attnum)              AS comment,\n" +
                "       format_type(a.atttypid, a.atttypmod)               AS type,\n" +
                "       a.attnotnull                                       AS notNull,\n" +
                "       a.attnum                                           As num\n" +
                "FROM pg_class AS c,\n" +
                "     pg_attribute AS a\n" +
                "WHERE c.relname =:tableName\n" +
                "  AND a.attrelid = C.oid\n" +
                "  AND a.attnum > 0\n" +
                "ORDER BY c.relname, a.attnum";
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tableName", tableName);
        return this.query(sql, queryParams);
    }

}
