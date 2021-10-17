package com.sprout.shiro;

import com.sprout.system.entity.Group;
import com.sprout.system.entity.Resource;
import com.sprout.system.entity.Role;
import com.sprout.system.entity.User;
import com.sprout.system.utils.Status;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 自定义Authentication对象，使得Subject除了携带用户的登录名外还可以携带更多信息.
 */
public class ShiroUser implements Serializable {

	private static final long serialVersionUID = -1373760761780840081L;

	public String userId;
	
	public String loginName;
	
	public String name;
	
	public List<Resource> resources;
	
	public List<Role> roles;

	private Date startLoginTime;

	private String ip;

	private String sessionId;

	private List<Group> groupList = new ArrayList<>();

	private Group group;

	private User user;

	public ShiroUser(User user) {
		this.user = user;
		this.userId = user.getId().toString();
		this.loginName = user.getLoginName();
		this.name = user.getName();
		this.group = user.getGroup();
	}

	public ShiroUser(String userId, String loginName, String name,
					 List<Resource> resources) {
		this.userId = userId;
		this.loginName = loginName;
		this.name = name;
		this.resources = resources;
	}

	public ShiroUser(String userId, String loginName, String name, Group group) {
		this.userId = userId;
		this.loginName = loginName;
		this.name = name;
		this.group = group;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public boolean isSuperAdmin() {
		return User.ADMIN.equalsIgnoreCase(this.loginName);
	}
	public String getUserId() {
		return userId;
	}

	public String getName() {
		return name;
	}

	public String getLoginName() {
		return loginName;
	}

	public List<Resource> getResources() {
		return resources;
	}

	public void setResources(List<Resource> resources) {
		this.resources = resources;
	}
	
	public List<Role> getRoles() {
		return roles;
	}

	public Date getStartLoginTime() {
		return startLoginTime;
	}

	public void setStartLoginTime(Date startLoginTime) {
		this.startLoginTime = startLoginTime;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getSessionId() {
		return sessionId;
	}

	public List<Group> getGroupList() {
		List<Group> list = new ArrayList<>();
		if (group != null) {
			list.add(group);
			list.addAll(group.getChildList(Status.ENABLE));
		}
		return list;
	}

	public Group getGroup() {
		return group;
	}

	/**
	 * 本函数输出将作为默认的<shiro:principal/>输出.
	 */
	@Override
	public String toString() {
		return name;
	}

	/**
	 * 重载hashCode,只计算loginName;
	 */
	@Override
	public int hashCode() {
		return Objects.hashCode(loginName);
	}

	/**
	 * 重载equals,只计算loginName;
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ShiroUser other = (ShiroUser) obj;
		if (loginName == null) {
			return other.loginName == null;
		} else return loginName.equals(other.loginName);
	}
}
