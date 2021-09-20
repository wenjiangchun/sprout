package com.sprout.core.service;

import com.sprout.core.jpa.entity.AbstractLoginDeletedEntity;
import com.sprout.core.jpa.repository.BaseRepository;
import com.sprout.core.jpa.repository.HazeSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.*;

@Transactional(rollbackFor = Exception.class)
public abstract class AbstractLogicDeletedService<T extends AbstractLoginDeletedEntity<PK>, PK extends Serializable> implements BaseService<T, PK> {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    private BaseRepository<T,PK> dao;

    public AbstractLogicDeletedService(BaseRepository<T, PK> dao) {
        this.dao = dao;
    }

    /**
     * 查询所有对象
     * @return T 对象集合
     */
    @Override
    public List<T> findAll() {
        return this.dao.findAll(configFilter());
    }

    @Override
    public List<T> findAll(Sort sort) {
        return this.dao.findAll(configFilter(), sort);
    }

    /**
     * 根据Id查找对象
     * @param id 对象Id
     * @return T 对象
     */
    @Override
    public T findById(PK id) {
        Specification<T> specification = configFilter();
        Specification<T> idSpec = (Specification<T>) (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("id"), id);
        return this.dao.findOne(specification.and(idSpec)).orElse(null);
    }

    /**
     * 保存 T 对象
     * @param t 对象
     * @return T 对象
     */
    @Override
    @Transactional(rollbackFor=Exception.class)
    public T save(T t) throws Exception {
        Date d = new Date();
        /*if (t.isNew() && t.getCreateTime() == null) {
            t.setCreateTime(d);
            t.setDeleted(false);
            logger.info("saving {}", t);
        } else {
            t.setDeleted(false);
            t.setUpdateTime(d);
            logger.info("updating {}", t);
        }*/
        return this.dao.save(t);
    }

    @Override
    @Transactional(rollbackFor=Exception.class)
    public void delete(T t) throws Exception {
        t.setDeleted(true);
        t.setDeleteTime(new Date());
        logger.info("logic deleting {}", t);
        this.dao.save(t);
    }

    @Override
    @Transactional(rollbackFor=Exception.class)
    public void deleteIds(PK[] ids) throws Exception {
        for (PK id : ids) {
            this.deleteById(id);
        }
    }

    @Override
    @Transactional(rollbackFor=Exception.class)
    public void deleteById(PK id) throws Exception {
        Optional<T> optional = this.dao.findById(id);
        if (optional.isPresent()) {
            this.delete(optional.get());
        }
    }

    /**
     * 根据Id数组批量删除对象
     * @param ids Id数组
     */
    @Override
    @Transactional(rollbackFor=Exception.class)
    public void batchDelete(PK[] ids) throws Exception {
        for (PK id : ids) {
            deleteById(id);
        }
    }

    /**
     * 分页查询 <T> 列表对象
     * @param p 分页对象
     * @return 分页<T>列表对象
     */
    @Override
    public Page<T> findPage(Pageable p) {
        return this.dao.findAll(configFilter(), p);
    }

    /**
     * 根据查询参数分页查询列表信息
     * @param queryVirables 查询参数
     * @param pageable 分页对象
     * @return 分页对象
     */
    @Override
    public Page<T> findPage(Pageable pageable, Map<String, Object> queryVirables) {
        Specification<T> spec = new HazeSpecification<>(queryVirables);
        spec = spec.and(configFilter());
        return this.dao.findAll(spec, pageable);
    }

    /**
     * 根据查询参数查询所有<T>对象信息
     * @param queryParams 查询参数
     * @return {@code List<T>}
     */
    @Override
    public List<T> findAll(Map<String, Object> queryParams) {
        Specification<T> spec = new HazeSpecification<>(queryParams);
        spec = spec.and(configFilter());
        return this.dao.findAll(spec);
    }

    /**
     * 根据属性名查询对象信息列表
     * @param propertyName  对象属性名
     * @param value         属性值
     * @return  {@code List<T>}
     */
    @Override
    public List<T> findByProperty(String propertyName, Object value, Sort... sorts) {
        return this.dao.findByProperty(propertyName, value, sorts);
    }

    @Override
    public List<T> findByJql(String jql, Map<String, Object> queryParams) {
        return this.dao.findByJql(jql, queryParams);
    }

    @Override
    public List<T> findBySql(String sql, Map<String, Object> queryParams) {
        return this.dao.findBySql(sql, queryParams);
    }

    private Specification<T> configFilter() {
        Map<String, Object> filter = new HashMap<>();
        filter.put("deleted_isFalse", true);
        return new HazeSpecification<>(filter);
    }

    @Override
    public T findOneByProperty(String propertyName, Object value) {
        return this.dao.findOneByProperty(propertyName, value);
    }

}