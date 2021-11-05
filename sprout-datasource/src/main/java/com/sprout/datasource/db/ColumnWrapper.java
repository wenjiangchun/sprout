package com.sprout.datasource.db;

public class ColumnWrapper {

    private String columnName;

    private String columnType;

    private String comment;

    private int length;

    private boolean notNull;

    private int num;

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public boolean isNotNull() {
        return notNull;
    }

    public void setNotNull(boolean notNull) {
        this.notNull = notNull;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    @Override
    public String toString() {
        return "ColumnWrapper{" +
                "columnName='" + columnName + '\'' +
                ", columnType='" + columnType + '\'' +
                ", comment='" + comment + '\'' +
                ", length=" + length +
                ", notNull=" + notNull +
                ", num=" + num +
                '}';
    }
}
