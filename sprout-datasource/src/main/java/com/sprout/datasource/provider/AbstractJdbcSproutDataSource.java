package com.sprout.datasource.provider;

import com.sprout.datasource.ds.entity.DataSourceMeta;
import org.springframework.beans.BeanUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public abstract class AbstractJdbcSproutDataSource implements SproutDataSource {

    private DataSourceMeta dataSourceMeta;

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @SuppressWarnings("unchecked")
    public AbstractJdbcSproutDataSource(DataSourceMeta dataSourceMeta) {
        this.dataSourceMeta = dataSourceMeta;
        Class<Driver> driverClass;
        try {
            driverClass = (Class<Driver>) Class.forName(dataSourceMeta.getDataSourceMetaType().getDriverClass());
            Driver driver = BeanUtils.instantiateClass(driverClass);
            DataSource dateSource = new SimpleDriverDataSource(driver,dataSourceMeta.getUrl(), dataSourceMeta.getUserName(), dataSourceMeta.getPassword());
            this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dateSource);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Connection getConnection() throws Exception{
        return this.namedParameterJdbcTemplate.getJdbcTemplate().getDataSource().getConnection();
    }

    @Override
    public void execute(String sql) throws Exception {

    }

    @Override
    public <T> List<T> query(String sql, Map<Integer, Object> queryParams, Class<T> tClass) throws Exception {
        return null;
    }

    @Override
    public List<Map<String, Object>> query(String sql, Map<String, Object> queryParams) throws Exception {
        return namedParameterJdbcTemplate.queryForList(sql, queryParams);
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

    protected NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
        return this.namedParameterJdbcTemplate;
    }

}
