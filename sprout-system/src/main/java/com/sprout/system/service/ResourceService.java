package com.sprout.system.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.sprout.common.util.SproutStringUtils;
import com.sprout.core.service.AbstractBaseService;
import com.sprout.system.dao.ResourceDao;
import com.sprout.system.entity.Resource;
import com.sprout.system.entity.Role;
import com.sprout.system.utils.ResourceType;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * 资源操作业务类
 * @author sofar
 *
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ResourceService extends AbstractBaseService<Resource, Long> {

	private ResourceDao resourceDao;

	public ResourceService(ResourceDao resourceDao) {
		super(resourceDao);
		this.resourceDao = resourceDao;
	}

	/**
	 * 保存或更新资源对象 同时清空shiro缓存对象
	 * @param resource 资源对象
	 * @throws Exception
	 */
	@CacheEvict(value="shiroCache",allEntries=true)
	public Resource saveOrUpdate(Resource resource) throws Exception {
		Assert.notNull(resource);
		if (resource.isNew()) { //保存资源对象
			logger.info("save resource：{}", resource);
		} else { //更新资源对象
			logger.info("update resource：{}", resource);
		}
		return this.save(resource);
	}
	
	/**
	 * 获取所有资源权限不为空的权限名称  
	 * @return 资源权限名称集合
	 */
	@Transactional(readOnly = true)
	public List<String> findAllPermission() {
		List<String> permissionList = new ArrayList<String>();
		List<Resource> resourceList = this.resourceDao.findByPermissionIsNotNull();
		for (Resource resource : resourceList) {
			if (SproutStringUtils.isNotEmpty(resource.getPermission())) {
				permissionList.add(resource.getPermission());
			}
		}
		return permissionList;
	}

	/**
	 * 批量删除资源对象 同时清空shiro缓存对象
	 * @param ids 资源ID数组
	 * @throws Exception
	 */
	@CacheEvict(value="shiroCache",allEntries=true)
	@Override
	public void batchDelete(Long[] ids) throws Exception {
		for (Long id : ids) {
			Resource resource = this.findById(id);
			Set<Role> roles = resource.getRoles();
			for (Role role : roles) {
				role.removeResource(resource);
			}
			this.deleteById(id);
		}
	}
	
	/**
	 * 根据资源对象类型查找资源对象
	 * @param resourceType 资源类型
	 * @return 资源信息列表
	 */
	@Transactional(readOnly = true)
	public List<Resource> findByResourceType(ResourceType resourceType) {
		return this.resourceDao.findByResourceType(resourceType);
	}

	/**
	 * 查找菜单资源
	 * @return 菜单资源信息列表
	 */
	@Transactional(readOnly = true)
	public List<Resource> findMenuResources() {
		return findByResourceType(ResourceType.MENU);
	}

	@Transactional(readOnly = true)
	public List<Resource> findAllBySn(boolean asc) {
		if (asc) {
			return this.resourceDao.findAll(Sort.by(Sort.Direction.ASC, "sn"));
		} else {
			return this.resourceDao.findAll(Sort.by(Sort.Direction.DESC, "sn"));
		}
	}
}
