package com.sprout.data.ds.service;

import com.sprout.core.service.AbstractBaseService;
import com.sprout.data.ds.dao.DataSourceMetaDao;
import com.sprout.data.ds.entity.DataSourceMeta;
import com.sprout.data.ds.provider.JdbcSproutDataSource;
import com.sprout.data.ds.provider.SproutDataSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class DataSourceMetaService extends AbstractBaseService<DataSourceMeta, Long> {

    private static Map<String, SproutDataSource> DATA_SOURCE_MAP = new HashMap<>();

    private DataSourceMetaDao dataSourceMetaDao;

    public DataSourceMetaService(DataSourceMetaDao dataSourceMetaDao) {
        super(dataSourceMetaDao);
        this.dataSourceMetaDao = dataSourceMetaDao;
        List<DataSourceMeta> dataSourceMetaList = this.dataSourceMetaDao.findAll();
        for (DataSourceMeta dataSourceMeta : dataSourceMetaList) {
            putDataSourceMeta(dataSourceMeta);
        }
    }

    public Map<String, SproutDataSource> getSourceMetaMap() {
        return DATA_SOURCE_MAP;
    }

    @Transactional(readOnly = true)
    public void registerSourceMeta(DataSourceMeta dataSourceMeta) {
        Objects.requireNonNull(dataSourceMeta, "数据源不能为空");
        this.dataSourceMetaDao.save(dataSourceMeta);
        putDataSourceMeta(dataSourceMeta);
    }

    private void putDataSourceMeta(DataSourceMeta dataSourceMeta) {
        switch (dataSourceMeta.getDataSourceMetaType()) {
            case MySQL5:
            case MySQL8:
            case Oracle:
            case PostgreSQL:
            case Impala:
                DATA_SOURCE_MAP.put(dataSourceMeta.getName(), new JdbcSproutDataSource(dataSourceMeta));
                break;
            default:
                break;
        }
    }

    public boolean testConnection(DataSourceMeta dataSourceMeta) throws Exception {
        if (DATA_SOURCE_MAP.containsKey(dataSourceMeta.getName())) {
            return DATA_SOURCE_MAP.get(dataSourceMeta.getName()).getConnectState();
        } else {
            boolean state = false;
            switch (dataSourceMeta.getDataSourceMetaType()) {
                case MySQL5:
                case MySQL8:
                case Oracle:
                case PostgreSQL:
                case Impala:
                    JdbcSproutDataSource dataSource = new JdbcSproutDataSource(dataSourceMeta);
                    state = dataSource.getConnectState();
                    if (state) {
                        dataSource.closeConnection();
                    }
                    break;
                default:
                    break;
            }
            return state;
        }
    }
}
