package com.sprout.core.jpa.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 系统持久化实体接口定义
 *
 * @param <PK>
 */
public interface BaseCommonEntity<PK extends Serializable> extends BaseEntity<PK> {

	Date getCreateTime();

	void setCreateTime(Date createTime);

	Date getUpdateTime();

	void setUpdateTime(Date updateTime);

}
