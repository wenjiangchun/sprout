package com.sprout.data.provider;

import com.sprout.data.ds.entity.DataSourceMeta;
import com.sprout.data.ds.util.DataSourceMetaType;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

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

    @Override
    public Page<?> getDataPage(PageRequest p, String tableName) throws Exception {
        String countSQL = "select count(1) from " + tableName;
        Map<String, Object> queryParams = new HashMap<>();
        //queryParams.put("tableName", tableName);
        int totalCount = getNamedParameterJdbcTemplate().queryForObject(countSQL, queryParams, Integer.class);
        if (totalCount > 0) {
            String pageSQL = "select * from " + tableName ;
            if (this.dataSourceMeta.getDataSourceMetaType().equals(DataSourceMetaType.Impala)) {
                Map<String, Object> firstColumn = this.getColumns(tableName).get(0);
                pageSQL += " order by " + firstColumn.get("name");
            }
            pageSQL += " limit :pageSize offset :numStart";
            int numStart = (p.getPageNumber()) * p.getPageSize();
            queryParams.put("numStart", numStart);
            queryParams.put("pageSize", p.getPageSize());
            List<Map<String, Object>> result = getNamedParameterJdbcTemplate().queryForList(pageSQL, queryParams);
            return new PageImpl<>(result, p, totalCount);
        } else {
            return new PageImpl<>(new ArrayList<>(), p, 0);
        }
    }
}
