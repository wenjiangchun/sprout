package com.sprout.data.provider.impala;

import com.sprout.data.db.TableWrapper;
import com.sprout.data.ds.entity.DataSourceMeta;
import com.sprout.data.provider.AbstractJdbcSproutDataSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    /*@Override
    public List<ColumnWrapper> getColumnList(String tableName) throws Exception {
        String sql = "describe " + tableName;
        PreparedStatement preparedStatement = getConnection().prepareStatement(sql);
        //preparedStatement.setString(1, tableName);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            ColumnWrapper columnWrapper = new ColumnWrapper();
            //resultSet.getMetaData().
            *//*columnWrapper.setColumnName(resultSet.getString(1));
            columnWrapper.setColumnType(resultSet.getString(2));
            columnWrapper.setComment(resultSet.getString(3));
            columnWrapper.setNotNull(!resultSet.getBoolean(4));
            columnWrapper.setDefaultValue(resultSet.getString(5));
            columnWrapper.setEncoding(resultSet.getString(6));
            columnWrapper.setCompression(resultSet.getString(7));
            columnWrapper.setBlockSize(resultSet.getInt(8));*//*
        }
        resultSet.close();
        preparedStatement.close();
        return null;
    }*/

    @Override
    public List<Map<String, Object>> getColumns(String tableName) throws Exception {
        /*String sql = "describe " + tableName;
        PreparedStatement preparedStatement = getConnection().prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        ResultSetMetaData metaData = resultSet.getMetaData();
        List<Map<String, Object>> list = new ArrayList<>();
        while (resultSet.next()) {
            Map<String, Object> columnDt = new HashMap<>();
            for (int i = 0; i < metaData.getColumnCount(); i++) {
                String label = metaData.getColumnName(i);
                int columnType = metaData.getColumnType(i);
                switch (columnType) {
                    case Types.SMALLINT:
                    case Types.BIGINT:
                    case Types.INTEGER:
                        columnDt.put(label, resultSet.getInt(i));
                        break;
                    case Types.BOOLEAN:
                        columnDt.put(label, resultSet.getBoolean(i));
                        break;
                    default:
                        columnDt.put(label, resultSet.getString(i));
                }

            }
            list.add(columnDt);
        }
        resultSet.close();
        preparedStatement.close();*/
        return this.query("describe " + tableName, new HashMap<>());
        //return list;
    }

}
