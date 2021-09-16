package com.sprout.dlyy.kudu;

public class KuduTableMeta {
    private String tableName;
    private int rowCount;
    private int tableSize;

    private String tableId;

    private String comment;

    private int numReplicate;

    public int getNumReplicate() {
        return numReplicate;
    }

    public void setNumReplicate(int numReplicate) {
        this.numReplicate = numReplicate;
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public long getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    public long getTableSize() {
        return tableSize;
    }

    public void setTableSize(int tableSize) {
        this.tableSize = tableSize;
    }
}
