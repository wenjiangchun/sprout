package com.sprout.core.service;

import com.sprout.core.jpa.entity.BaseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface BaseService<T extends BaseEntity<PK>, PK extends Serializable> {

    List<T> findAll();

    List<T> findAll(Sort sort);

    T findById(PK id);

    T save(T t) throws Exception;

    void delete(T t) throws Exception;

    void deleteIds(PK[] ids) throws Exception;

    void deleteById(PK id) throws Exception;

    void batchDelete(PK[] ids) throws Exception;

    Page<T> findPage(Pageable p);

    Page<T> findPage(Pageable pageable, Map<String, Object> queryParams);

    List<T> findAll(Map<String, Object> queryParams);

    List<T> findByProperty(String propertyName, Object value, Sort... sorts);

    List<T> findByJql(String jql, Map<String, Object> queryParams);

    List<T> findBySql(String sql, Map<String, Object> queryParams);
}
