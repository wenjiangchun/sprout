package com.sprout.system.dao;

import com.sprout.core.jpa.repository.BaseRepository;
import com.sprout.system.entity.Dict;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * 字典DAO接口定义类
 *
 * @author wenjiangchun
 */
@Repository
public interface DictDao extends BaseRepository<Dict, Long>, DictRepository {

    /**
     * 根据根节点字典代码查找启用状态子字典信息
     *
     * @param code 字典代码
     * @return 字典对象集合
     */
    @Query("from Dict d where d.parent.code=:code and d.enabled=true and d.parent.parent = null order by d.sn asc")
    List<Dict> findChildsByRootCode(@Param("code") String code);

    /**
     * 设置字典是否启用
     *
     * @param id        字典ID
     * @param enabled  true:启用， false:禁用
     * @return
     */
    @Modifying
    @Query("update Dict d set d.enabled=:enabled where d.id=:id")
    void updateDictionaryEnabled(@Param("id") Long id, @Param("enabled") Boolean enabled);

    /**
     * 根据字典类别上级代码和字典代码查找字典对象。
     * <p><b>说明：</b>该方法将从字典类别所在父节点查询符合条件的字典对象。</p>
     *
     * @param parentCode 字典所在父节点代码
     * @param code       字典代码
     * @return 父节点下字典代码=参数code的字典对象
     */
    Dict findByParentCodeAndCode(String parentCode, String code);

    @Query("from Dict d where d.parent.id=:parentId and d.enabled=true order by d.sn asc")
    List<Dict> findByParentId(@Param("parentId") Long parentId);
}
