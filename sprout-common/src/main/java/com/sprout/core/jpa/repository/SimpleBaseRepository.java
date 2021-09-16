package com.sprout.core.jpa.repository;

import com.sprout.core.jpa.entity.AbstractLoginDeletedEntity;
import com.sprout.core.jpa.entity.BaseEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.Parameter;
import javax.persistence.Query;
import javax.persistence.criteria.*;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * BaseRepository接口实现类
 * @param <T>
 * @param <ID>
 */
@NoRepositoryBean
public class SimpleBaseRepository<T extends BaseEntity<ID>, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements BaseRepository<T, ID> {

    private EntityManager em;

    private JpaEntityInformation<T, ?> entityInformation;

    public SimpleBaseRepository(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityInformation = entityInformation;
        this.em = entityManager;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<T> findByProperty(String propertyName, Object value, Sort... sorts) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(entityInformation.getJavaType());
        Root<T> root = criteriaQuery.from(entityInformation.getJavaType());
        String[] propertyNames = propertyName.split("\\.");
        Path<?> path = root.get(propertyNames[0]);
        for (String name : propertyNames) {
            if (!name.equals(propertyNames[0])) {
                path = path.get(name);
            }
        }
        Predicate predicate = value != null ? criteriaBuilder.equal(root.get(propertyName), value) : criteriaBuilder.isNull(root.get(propertyName));
        if (AbstractLoginDeletedEntity.class.isAssignableFrom(entityInformation.getJavaType())) {
            criteriaQuery.where(predicate, criteriaBuilder.equal(root.get("deleted"), false));
        } else {
            criteriaQuery.where(predicate);
        }
        for (Sort sort : sorts) {
            for (Sort.Order order : sort) {
                Sort.Direction direction = order.getDirection();
                String orderPropertyName = order.getProperty();
                switch (direction) {
                    case ASC:
                        criteriaQuery.orderBy(criteriaBuilder.asc(root.get(orderPropertyName)));
                        break;
                    case DESC:
                        criteriaQuery.orderBy(criteriaBuilder.desc(root.get(orderPropertyName)));
                        break;
                    default:
                        criteriaQuery.orderBy(criteriaBuilder.desc(root.get(orderPropertyName)));
                }
            }
        }
        Query query = em.createQuery(criteriaQuery);
        return query.getResultList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public T findOneByProperty(String propertyName, Object value, Sort... sorts) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(entityInformation.getJavaType());
        Root<T> root = criteriaQuery.from(entityInformation.getJavaType());
        String[] propertyNames = propertyName.split("\\.");
        Path<?> path = root.get(propertyNames[0]);
        for (String name : propertyNames) {
            if (!name.equals(propertyNames[0])) {
                path = path.get(name);
            }
        }
        Predicate predicate = value != null ? criteriaBuilder.equal(root.get(propertyName), value) : criteriaBuilder.isNull(root.get(propertyName));
        if (AbstractLoginDeletedEntity.class.isAssignableFrom(entityInformation.getJavaType())) {
            criteriaQuery.where(predicate, criteriaBuilder.equal(root.get("deleted"), false));
        } else {
            criteriaQuery.where(predicate);
        }
        for (Sort sort : sorts) {
            for (Sort.Order order : sort) {
                Sort.Direction direction = order.getDirection();
                String orderPropertyName = order.getProperty();
                switch (direction) {
                    case ASC:
                        criteriaQuery.orderBy(criteriaBuilder.asc(root.get(orderPropertyName)));
                        break;
                    case DESC:
                        criteriaQuery.orderBy(criteriaBuilder.desc(root.get(orderPropertyName)));
                        break;
                    default:
                        criteriaQuery.orderBy(criteriaBuilder.desc(root.get(orderPropertyName)));
                }
            }
        }
        Query query = em.createQuery(criteriaQuery);
        return (T) query.getSingleResult();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<T> findByJql(String jpql, Map<String, Object> queryParams) {
        Assert.notNull(jpql, "查询语句不能为空!");
        Query query = em.createQuery(jpql);
        setParams(queryParams, query);
        return query.getResultList();
    }

    private void setParams(Map<String, Object> queryParams, Query query) {
        for (Parameter param : query.getParameters()) {
            String name = param.getName();
            if (StringUtils.hasText(name)) {
                query.setParameter(name, queryParams.get(name));
            } else {
                throw new IllegalArgumentException("查询出错, Query查询语句命名参数不能为空");
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<T> findBySql(String sql, Map<String, Object> queryParams) {
        Assert.notNull(sql, "查询语句不能为空!");
        Query query = em.createNativeQuery(sql);
        setParams(queryParams, query);
        return query.getResultList();
    }
}