package com.sprout.datasource.data.db;

import java.util.ArrayList;
import java.util.List;

public class TableWrapper {

    //表名称
    private String tableName;

    //空间大小
    private long tableSize;

    //表空间名称
    private String tableSpaceName;

    private List<ColumnWrapper> columnList = new ArrayList<>();

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public long getTableSize() {
        return tableSize;
    }

    public void setTableSize(long tableSize) {
        this.tableSize = tableSize;
    }

    public String getTableSpaceName() {
        return tableSpaceName;
    }

    public void setTableSpaceName(String tableSpaceName) {
        this.tableSpaceName = tableSpaceName;
    }

    public List<ColumnWrapper> getColumnList() {
        return columnList;
    }

    public void setColumnList(List<ColumnWrapper> columnList) {
        this.columnList = columnList;
    }
}
