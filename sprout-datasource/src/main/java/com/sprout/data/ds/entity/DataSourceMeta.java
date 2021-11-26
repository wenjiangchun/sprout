package com.sprout.data.ds.entity;

import com.sprout.common.util.SproutStringUtils;
import com.sprout.core.jpa.entity.AbstractBaseEntity;
import com.sprout.data.ds.util.DataSourceMetaType;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "d_data_source_meta")
public class DataSourceMeta extends AbstractBaseEntity<Long> {

    private String name;

    private String ip;

    private String userName;

    private String password;

    private String port;

    private String url;

    private boolean state = false;

    private DataSourceMetaType dataSourceMetaType;

    private String schema;

    private String remark;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getUrl() {
        if (SproutStringUtils.isNoneBlank(this.url)) {
            return url;
        } else {
            switch (this.dataSourceMetaType){
                case MySQL5:
                    String mysqlJdbc = "jdbc:mysql://%s:%s/";
                    url = String.format(mysqlJdbc, this.ip, this.port);
                    if (Objects.nonNull(this.schema)) {
                        url += this.schema;
                    }
                    break;
                case Impala:
                    String impalaTemplate = "jdbc:impala://%s:%s/";
                    url = String.format(impalaTemplate, this.ip, this.port);
                    if (Objects.nonNull(this.schema)) {
                        url += this.schema;
                    }
                    break;
                case PostgreSQL:
                    String postgresqlTemplate = "jdbc:postgresql://%s:%s";
                    //url = "jdbc:impala://node-3:21050";
                    url = String.format(postgresqlTemplate, this.ip, this.port);
                    if (Objects.nonNull(this.schema)) {
                        url += "/" + this.schema;
                    }
                    break;
                default:
                    break;
            }
        }
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public DataSourceMetaType getDataSourceMetaType() {
        return dataSourceMetaType;
    }

    public void setDataSourceMetaType(DataSourceMetaType dataSourceMetaType) {
        this.dataSourceMetaType = dataSourceMetaType;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    @Override
    public String toString() {
        return "DataSourceMeta{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", ip='" + ip + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", port='" + port + '\'' +
                ", url='" + url + '\'' +
                ", state=" + state +
                ", dataSourceMetaType=" + dataSourceMetaType +
                ", schema='" + schema + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}