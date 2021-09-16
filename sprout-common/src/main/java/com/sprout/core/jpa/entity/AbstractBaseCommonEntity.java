package com.sprout.core.jpa.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@MappedSuperclass
public abstract class AbstractBaseCommonEntity<PK extends Serializable> extends AbstractBaseEntity<PK> {

    private static final long serialVersionUID = 1L;

    protected Date createTime;

    protected Date updateTime;

    public void setId(final PK id) {
        this.id = id;
    }

    @Column(updatable = false)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

}