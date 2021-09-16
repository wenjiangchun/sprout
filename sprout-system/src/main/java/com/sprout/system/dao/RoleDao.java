package com.sprout.system.dao;

import java.util.List;

import com.sprout.core.jpa.repository.BaseRepository;
import com.sprout.system.entity.Role;
import org.springframework.stereotype.Repository;


/**
 * 角色操作接口定义类
 * 
 * @author sofar
 *
 */
@Repository
public interface RoleDao extends BaseRepository<Role, Long> {

	/**
	 * 根据角色状态查找角色对象
	 * @param enabled 状态值
	 * @return 角色对象集合
	 */
	List<Role> findByEnabled(boolean enabled);

	/**
	 * 根据角色英文名称查找角色对象
	 * @param code 角色代码
	 * @return 角色对象
	 */
	Role findByCode(String code);
	
}
