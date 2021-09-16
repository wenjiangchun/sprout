package com.sprout.core.jpa.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 持久化实体类基类，该类定义了实体主键Id生成策略和根据Id实现基本hashCode()和equals()方法，
 * 并定义抽象toString()方法，在具体使用中项目中实体继承该类并实现具体toString()方法即可，
 * 如需要重写hashCode()和equals()方法，要确保如果实体类之间equals()为true则实体类之间的haseCode()也一定是相等的。
 * 
 *<p><b>说明：</b>子类如果需要使用不同ID生成策略，则只需重写getId()方法并在此方法上指定主键生成策略即可.</p>
 * @param <PK> 实体对象主键
 * @author sofar
 */
@MappedSuperclass
public abstract class AbstractLoginDeletedEntity<PK extends Serializable> implements BaseEntity<PK> {

    private static final long serialVersionUID = 1L;

    /**
     * 唯一标识
     */
    protected PK id;

    protected Date createTime = new Date();

    protected Date updateTime;

    protected Date deleteTime;

    protected Boolean deleted = false;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public PK getId() {
        return id;
    }

    public void setId(final PK id) {
        this.id = id;
    }

    public Date getDeleteTime() {
        return deleteTime;
    }

    public void setDeleteTime(Date deleteTime) {
        this.deleteTime = deleteTime;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public int hashCode() {
        int hashCode = 17;
        hashCode += null == getId() ? 0 : getId().hashCode() * 31;
        return hashCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (!getClass().equals(obj.getClass())) {
            return false;
        }
        AbstractLoginDeletedEntity<?> that = (AbstractLoginDeletedEntity<?>) obj;
        return null != this.getId() && this.getId().equals(that.getId());
    }

   public abstract String toString();
}