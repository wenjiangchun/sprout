package com.sprout.system.service;

import java.util.*;

import com.sprout.core.service.AbstractBaseService;
import com.sprout.system.dao.RoleDao;
import com.sprout.system.entity.Resource;
import com.sprout.system.entity.Role;
import com.sprout.system.entity.User;
import com.sprout.system.exception.RoleExistException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 角色对象Service
 * @author sofar
 *
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class RoleService extends AbstractBaseService<Role, Long> {
	
	private RoleDao roleDao;

	public RoleService(RoleDao roleDao) {
		super(roleDao);
		this.roleDao = roleDao;
	}

	public List<Role> findByEnabled(boolean enabled) {
		return this.roleDao.findByEnabled(enabled);
	}
	
	/**
	 * 根据角色状态获取系统中所有的角色名称
	 * @param enabled 角色状态
	 * @return 角色名称集合
	 */
	public List<String> findAllRoleNameByEnabled(boolean enabled) {
		List<String> roleNameList = new ArrayList<>();
		List<Role> roleList = findByEnabled(enabled);
		for (Role role : roleList) {
			roleNameList.add(role.getName());
		}
		return roleNameList;
	}
	/**
	 * 保存角色对象，保存前会校验角色名称是否存在，如果角色名称已存在则抛出RoleExistException
	 * @param role 角色对象
	 * @return 
	 * @throws RoleExistException 角色已存在对象
	 */
	@CacheEvict(value="shiroCache",allEntries=true)
	public Role saveOrUpdate(Role role) throws RoleExistException {
		Objects.requireNonNull(role.getName(), "角色不能为空");
		Date date = new Date();
		//role.setUpdateTime(date);
		Role r = this.roleDao.findByCode(role.getCode());
		//判断角色是否已包含ID
		if (role.isNew()) {
			//判断数据库中是否已存在对应code对象
			if (r != null) {
				logger.error("角色保存失败，角色代码{}已存在！" + role.getCode());
				throw new RoleExistException("角色代码" + role.getCode() + "已存在");
			} else {
				//role.setCreateTime(date);
			}
		} else {
			if (r != null && !r.getId().equals(role.getId())) {
				logger.error("角色保存失败，角色代码{}已存在！" + role.getCode());
			} else {
				//执行更新操作 对原先角色资源重新授权
				role.setResources(this.findById(role.getId()).getResources());
			}
		}
		logger.info("保存角色成功，角色名为{}", role.getName());
		this.roleDao.save(role);
		return role;
	}
	
	/**
	 * 删除所有角色，同时删除和角色关联的用户，组织，资源的关联信息
	 */

	@CacheEvict(value="shiroCache",allEntries=true)
	public void deleteALl() throws Exception {
		List<Role> roleList = this.findAll();
		for (Role role : roleList) {
			delete(role.getId());
		}
	}
	
	/**
	 * 删除角色 同时删除和该角色关联的用户, 资源的关联信息
	 * @param id 角色Id
	 * @throws Exception 删除失败抛出异常
	 */
	@CacheEvict(value="shiroCache",allEntries=true)
	public void delete(Long id) throws Exception {
		Role role = this.findById(id);
		if (role != null) {
			//删除和角色关联的用户信息
			for (User user : role.getUsers()) {
				user.removeRole(role);
			}
			//删除和角色关联的资源信息
			role.setResources(null);
		}
		this.deleteById(id);
	}

	/**
	 * 批量删除角色 同时删掉用户 资源和角色的关联关系
	 * @param ids 角色Id集合
	 */
	@CacheEvict(value="shiroCache",allEntries=true)
	@Override
	public void batchDelete(Long[] ids) throws Exception {
		for (Long id : ids) {
			delete(id);
		}
	}

	@CacheEvict(value="shiroCache",allEntries=true)
	public void addResources(Long id, Long[] resourceIds) {
		Objects.requireNonNull(id, "角色id不能为null");
		Role role = this.findById(id);
		Set<Resource> resources = new HashSet<>();
		if (resourceIds == null) {
			role.setResources(null);
		} else {
			for (Long resourceId : resourceIds) {
				Resource resource = new Resource();
				resource.setId(resourceId);
				resources.add(resource);
			}
			role.setResources(resources);
		}
		this.roleDao.save(role);
	}
	
	/**
	 * 更新角色对象，同时清除shiro缓存
	 * @param role 角色对象
	 */
	@CacheEvict(value="shiroCache",allEntries=true)
	public Role updateRole(Role role) {
		return this.roleDao.save(role);
	}

    /**
     * 判断角色代码为{@code code}的角色对象是否存在
     * @param code 角色代码
     * @return 存在返回true,否则返回false
     */
	@Transactional(readOnly = true)
	public Boolean existCode(String code) {
		Role role = this.roleDao.findByCode(code);
		return role != null;
	}
	
}


