package com.sprout.system.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sprout.core.jpa.entity.AbstractBaseEntity;
import com.sprout.system.utils.ResourceType;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * 系统资源权限实体类
 * 
 * @author Sofar
 *
 */
@Entity
@Table(name="sys_resource")
@JsonIgnoreProperties(value={"childs","roles"})
public class Resource extends AbstractBaseEntity<Long> {
	
	private static final long serialVersionUID = 1L;

	private String name;
	
	private String permission;

	private String url;
	
	private ResourceType resourceType = ResourceType.MENU;
	
	/**
	 * 上级资源
	 */
	private Resource parent;

	private String icon;

	private Integer sn = 1;

	private String description;
	/**
	 * 子资源
	 */
	private Set<Resource> childs = new HashSet<>();
	
	/**
	 * 资源角色
	 */
	private Set<Role> roles = new HashSet<>();

	public Resource() {
	}

	@Column(length = 200)
	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	@Column(unique=true, length = 50)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(length = 200)
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Enumerated(EnumType.ORDINAL)
	public ResourceType getResourceType() {
		return resourceType;
	}

	public void setResourceType(ResourceType resourceType) {
		this.resourceType = resourceType;
	}

	@Column(length = 50)
	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	@Column(length = 2)
	public Integer getSn() {
		return sn;
	}

	public void setSn(Integer sn) {
		this.sn = sn;
	}

	@Column(length = 200)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@ManyToOne
	@JoinColumn(name="parent_id")
	public Resource getParent() {
		return parent;
	}

	public void setParent(Resource parent) {
		this.parent = parent;
	}

	@OneToMany(mappedBy="parent")
	public Set<Resource> getChilds() {
		return childs;
	}

	public void setChilds(Set<Resource> childs) {
		this.childs = childs;
	}

	@ManyToMany(mappedBy="resources")
	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	
	public void removeAllChilds() {
		for (Resource resource : this.childs) {
			resource.setParent(null);
		}
	}

	@Override
	public String toString() {
		String url = this.url != null ? this.url : "";
		return "Resource [permission=" + permission + ",name=" + name + ",resourceType=" + resourceType.getTypeName() + ",url=" + url + "]";
	}
}