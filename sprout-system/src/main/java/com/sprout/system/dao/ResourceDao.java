package com.sprout.system.dao;

import java.util.List;

import com.sprout.core.jpa.repository.BaseRepository;
import com.sprout.system.entity.Resource;
import com.sprout.system.utils.ResourceType;
import org.springframework.stereotype.Repository;

/**
 * 资源权限操作接口定义类
 * @author Sofar
 *
 */
@Repository
public interface ResourceDao extends BaseRepository<Resource, Long> {

	/**
	 * 根据资源类型查找资源信息
	 * @param type 资源类型
	 * @return 资源信息集合
	 */
	List<Resource> findByResourceType(ResourceType type);
	
	/**
	 * 查询permission不为空的资源信息
	 * @return 资源信息列表
	 */
	List<Resource> findByPermissionIsNotNull();
}
