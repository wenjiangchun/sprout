package com.sprout.datasource.ds.service;

import com.sprout.core.service.AbstractBaseService;
import com.sprout.datasource.ds.dao.DataSourceMetaDao;
import com.sprout.datasource.ds.entity.DataSourceMeta;
import com.sprout.datasource.provider.AbstractJdbcSproutDataSource;
import com.sprout.datasource.provider.SproutDataSource;
import com.sprout.datasource.provider.impala.ImpalaSproutDataSource;
import com.sprout.datasource.provider.postgresql.PostgresqlSproutDataSource;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class DataSourceMetaService extends AbstractBaseService<DataSourceMeta, Long> implements InitializingBean, DisposableBean {

    private static Map<String, SproutDataSource> DATA_SOURCE_MAP = new HashMap<>();

    private DataSourceMetaDao dataSourceMetaDao;

    public DataSourceMetaService(DataSourceMetaDao dataSourceMetaDao) {
        super(dataSourceMetaDao);
        this.dataSourceMetaDao = dataSourceMetaDao;
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
                DATA_SOURCE_MAP.put(dataSourceMeta.getName(), new PostgresqlSproutDataSource(dataSourceMeta));
                break;
            case Impala:
                DATA_SOURCE_MAP.put(dataSourceMeta.getName(), new ImpalaSproutDataSource(dataSourceMeta));
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
            SproutDataSource dataSource = null;
            switch (dataSourceMeta.getDataSourceMetaType()) {
                case MySQL5:
                case MySQL8:
                case Oracle:
                case PostgreSQL:
                    dataSource = new PostgresqlSproutDataSource(dataSourceMeta);
                    break;
                case Impala:
                    dataSource = new ImpalaSproutDataSource(dataSourceMeta);
                    break;
                default:
                    break;
            }
            if (dataSource != null) {
                state = dataSource.getConnectState();
                if (state) {
                    dataSource.closeConnection();
                }
            }
            return state;
        }
    }

    @Transactional(readOnly = true)
    public SproutDataSource getSproutDataSource(@PathVariable Long metaId) {
        DataSourceMeta dataSourceMeta = this.findById(metaId);
        return this.getSourceMetaMap().get(dataSourceMeta.getName());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        List<DataSourceMeta> dataSourceMetaList = this.dataSourceMetaDao.findAll();
        for (DataSourceMeta dataSourceMeta : dataSourceMetaList) {
            logger.debug("开始加载数据源...{}", dataSourceMeta);
            putDataSourceMeta(dataSourceMeta);
            logger.debug("成功加载数据源!{}", dataSourceMeta);
        }
    }

    @Override
    public void destroy() throws Exception {
        DATA_SOURCE_MAP.forEach((k, v) -> {
            try {
                if (v.getConnectState()) {
                    logger.debug("开始关闭数据源...{}", v.getDataSourceMeta());
                    v.closeConnection();
                    logger.debug("成功关闭数据源!{}", v.getDataSourceMeta());
                }
            } catch (Exception e) {
                logger.error("关闭数据源出错...{}", v.getDataSourceMeta(), e);
            }
        });
    }
}
