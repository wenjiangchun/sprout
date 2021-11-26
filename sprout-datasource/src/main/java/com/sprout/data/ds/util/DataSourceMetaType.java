package com.sprout.data.ds.util;

public enum DataSourceMetaType {

    MySQL5("com.mysql.jdbc.Driver"),

    MySQL8("com.mysql.cj.jdbc.Driver"),

    PostgreSQL("org.postgresql.Driver"),

    Oracle("oracle.jdbc.OracleDriver"),

    Impala("com.cloudera.impala.jdbc.Driver");

    private String driverClass;

    DataSourceMetaType(String driverClass) {

        this.driverClass = driverClass;
    }

    public String getDriverClass() {
        return driverClass;
    }

    public void setDriverClass(String driverClass) {
        this.driverClass = driverClass;
    }
}
