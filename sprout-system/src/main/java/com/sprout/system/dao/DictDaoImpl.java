package com.sprout.system.dao;

import com.sprout.system.entity.Dict;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import java.util.List;

/**
 * 自定义字典数据接口实现类
 * 
 * @author sofar
 *
 */
public class DictDaoImpl implements DictRepository {

	@PersistenceContext
	private EntityManager em;

    @SuppressWarnings("unchecked")
	@Override
    public List<Dict> findByRootCodeAndCode(String rootCode, String code) {
        String nativeSql = "SELECT T.* FROM SYS_DICT T WHERE T.CODE= ?1 START WITH ID=(SELECT ID FROM SYS_DICT WHERE CODE= ?2) CONNECT BY PRIOR ID = PARENT_ID ORDER BY T.SN ASC";
        Query query = em.createNativeQuery(nativeSql, Dict.class);
        query.setParameter(1, code);
        query.setParameter(2, rootCode);
        return query.getResultList();
    }
}
