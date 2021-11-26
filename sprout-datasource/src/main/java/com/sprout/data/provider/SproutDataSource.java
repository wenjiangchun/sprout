package com.sprout.data.provider;

import com.sprout.data.db.TableWrapper;
import com.sprout.data.ds.entity.DataSourceMeta;
import com.sprout.web.datatable.DataTablePage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

public interface SproutDataSource {

    Connection getConnection() throws Exception;

    void execute(String sql) throws Exception;

    <T> List<T> query(String sql, Map<Integer, Object> queryParams, Class<T> tClass) throws Exception;

    List<Map<String, Object>> query(String sql, Map<String, Object> queryParams) throws Exception;

    boolean getConnectState() throws Exception;

    void closeConnection() throws Exception;

    DataSourceMeta getDataSourceMeta();

    List<TableWrapper> getTables() throws Exception;

    /**
     * 获取表中字段信息
     * @param tableName 表名
     * @return 包含字段基础信息的列表
     * @throws Exception SQL异常
     */
    List<Map<String, Object>> getColumns(String tableName) throws Exception;

    /**
     * 获取指定表中所有数据
     * @param p 分页参数
     * @param tableName 表名
     * @return 表中分页数据
     */
    Page<?> getDataPage(PageRequest p, String tableName) throws Exception;
}
