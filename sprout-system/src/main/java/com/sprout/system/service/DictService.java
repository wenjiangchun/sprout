package com.sprout.system.service;

import com.sprout.core.service.AbstractBaseService;
import com.sprout.system.dao.DictDao;
import com.sprout.system.entity.Dict;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * <p>字典业务操作类  该类中涉及到缓存操作，因此在调用该类中参数为dictionary的方法时，如果该dictionary对象的parent不为空，则必须包含对应parent对象。</p>
 * 例如需要保存新的字典对象:<br>
 * <p>
 * 普通code: e.g.
 * <pre>
 * Dictionary dictionary = new Dictionary();
 * dictionary.setParent(new Dictionary(ParentId));
 * dictionaryService.saveOrUpdate(dictionary);
 * </pre>
 * <br>
 * 对于涉及到缓存code:
 * <pre>
 * Dictionary dictionary = new Dictionary();
 * Dictionary parent= dictionaryService.findById(parent); //important! 需要先加载parent对象，因为缓存采用parent.code作为缓存key
 * dictionary.setParent(parent);
 * dictionaryService.saveOrUpdate(dictionary);
 * </pre>
 *
 * @author Sofar
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class DictService extends AbstractBaseService<Dict, Long> {

    private DictDao dictDao;

    public DictService(DictDao dictDao) {
        super(dictDao);
        this.dictDao = dictDao;
    }

    /**
     * 根据配置名称查找配置对象
     *
     * @return 配置对象
     */
    @Transactional(readOnly = true)
    public Dict findByParentCodeAndCode(String parentCode, String code) {
        return this.dictDao.findByParentCodeAndCode(parentCode, code);
    }

    /**
     * 根据rootCode获取所有已启用子类字典,同时将查询结果缓存至名称为{@code dictionaryCache}的缓存,缓存key为{@code rootCode}
     *
     * @param rootCode 字典根节点code
     * @return 已启用子类字典集合
     */
    @Cacheable(value="dictionaryCache",key="#rootCode")
    @Transactional(readOnly = true)
    public List<Dict> findChildsByRootCode(String rootCode) {
        return this.dictDao.findChildsByRootCode(rootCode);
    }

    @CacheEvict(value = "dictionaryCache", condition = "#dict.parent != null", key = "#dict.parent.code")
    public Dict saveOrUpdate(Dict dict) throws Exception {
        Objects.requireNonNull(dict, "字典对象不能为空");
        Dict parent = dict.getParent();
        String parentCode = parent != null ? parent.getCode() : null;
        Dict c = this.dictDao.findByParentCodeAndCode(parentCode, dict.getCode());
        if (c != null && dict.isNew()) {
            logger.error("字典代码{}已存在，请重试！", dict.getCode());
            //throw new DictionaryNameExistException("配置名称" + Dictionary.getDictionaryName() + "已存在，请重试");
        } else {
            Date date = new Date();
            /*dict.setUpdateTime(date);
            if (dict.isNew()) { //保存配置对象
                dict.setCreateTime(date);
                logger.debug("添加字典信息，字典信息为：{}", dict);
            } else { //更新配置对象
                logger.debug("更新字典信息，字典信息为：{}", dict);
            }*/
        }
        return this.save(dict);
    }

    /**
     * 删除配置对象
     *
     * @param id 配置对象Id
     */
    public Dict deleteDictionary(Long id) {
        Objects.requireNonNull(id, "字典id不能为空");
        Dict Dict = this.findById(id);
        return this.deleteDictionary(Dict);
    }

    @CacheEvict(value = "dictionaryCache", allEntries = true)
    public void deleteDictionarys(Long[] ids) {
        for (Long id : ids) {
            this.deleteDictionary(id);
        }
    }

    @CacheEvict(value = "dictionaryCache", condition = "#dict.parent != null", key = "#dict.parent.code")
    public Dict deleteDictionary(Dict dict) {
        Objects.requireNonNull(dict, "字典对象不能为空");
        this.dictDao.delete(dict);
		logger.debug("删除字典信息，字典信息为：{}", dict);
        return dict;
    }

    @Override
    public void delete(Dict t) {
        deleteDictionary(t);
    }

    @Override
    public void deleteById(Long id) {
        deleteDictionary(id);
    }

    @CacheEvict(value = "dictionaryCache", allEntries = true)
    @Override
    public void batchDelete(Long[] ids) {
        deleteDictionarys(ids);
    }

    @CacheEvict(value = "dictionaryCache", condition = "#dict.parent != null", key = "#dict.parent.code")
    public Dict updateDictionary(Dict dict) throws Exception {
		Objects.requireNonNull(dict, "字典对象不能为空");
        Long id = dict.getId();
        dict = this.findById(id);
        this.save(dict);
        return dict;
    }

    @Transactional(readOnly = true)
    public Boolean isExistDictionaryCode(Long parentId, String code) {
        String parentCode = null;
        if (parentId != null) {
            Dict parent = this.findById(parentId);
            parentCode = parent != null ? parent.getCode() : null;
        }
        Dict dict = this.dictDao.findByParentCodeAndCode(parentCode, code);
        return dict != null;
    }

    /**
     * 更新字典启用状态, 同时根据字典是否有所属分类(即该对象的parent属性是否为null)更新字典所在缓存.</br>
     * 字典更新策略：字典状态和传递进来的isEnabled不同.</br>
     * 缓存更新策略：当dictionary.parent !=null && dictionary.parent.code != null.</br>
     * 注意：此时传递进来的Dictionary必须加载对应parent信息.</br>
     *
     * @param dict 字典信息(如果有parent则包含parent信息，更新字典缓存使用)
     * @param isEnabled  true表示启用 false表示禁用
     * @return Dictionary 字典对象
     */
    @CacheEvict(value = "dictionaryCache", condition = "#dict.parent != null && #dict.isEnabled != #isEnabled", key = "#dict.parent.code")
    public Dict updateDictionaryIsEnabled(Dict dict, boolean isEnabled) {
		Objects.requireNonNull(dict, "字典对象不能为空");
        if (dict.getEnabled() != isEnabled) {
            this.dictDao.updateDictionaryEnabled(dict.getId(), isEnabled);
        }
        return dict;
    }

    /**
     * 根据字典类别顶级代码和字典代码查找字典对象
     * <p><b>说明：</b>该方法将从字典类别根节点查询所有符合条件的字典对象，针对多级树状字典数据。</p>
     *
     * @param rootCode 字典所在根节点代码。
     * @param code     字典代码
     * @return 根节点下面所有字典代码=参数code的字典列表
     */
    @Transactional(readOnly = true)
    public List<Dict> findByRootCodeAndCode(String rootCode, String code) {
        return this.dictDao.findByRootCodeAndCode(rootCode, code);
    }

    @Transactional(readOnly = true)
    public List<Dict> findByParentId(Long parentId) {
        return this.dictDao.findByParentId(parentId);
    }
}
