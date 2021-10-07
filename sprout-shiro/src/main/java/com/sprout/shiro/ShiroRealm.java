package com.sprout.shiro;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import com.sprout.common.util.EncodeUtils;
import com.sprout.common.util.SproutStringUtils;
import com.sprout.system.entity.Role;
import com.sprout.system.entity.User;
import com.sprout.system.service.ResourceService;
import com.sprout.system.service.RoleService;
import com.sprout.system.service.UserService;
import com.sprout.system.utils.Status;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

public class ShiroRealm extends AuthorizingRealm {

	private static final String PERMS = "perms\\[(.*?)\\]";

	@Autowired
	private UserService userService;

	@Autowired
	private RoleService roleService;

	@Autowired
	private ResourceService resourceService;

	/**
	 * 认证回调函数,登录时调用.
	 */

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken authcToken) {
		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
		User user = userService.findByLoginName(token.getUsername());
		if (user == null) {
                throw new UnknownAccountException("用户名不存在!");

        }
		Status status = user.getStatus();
		if (status == Status.DISABLE) { //账户禁用
			throw new DisabledAccountException("账户被禁用!");
		}
         else if (status == Status.LOCK) { //账户冻结
			throw new LockedAccountException("账户被锁定!");
		}
		byte[] salt = EncodeUtils.decodeHex(user.getSalt());
         //获取用户所在机构
		if (user.getGroup() != null) {
			//加载子机构信息
			user.getGroup().getChildList(Status.ENABLE);
		}
		return new SimpleAuthenticationInfo(new ShiroUser(user),user.getPassword(), ByteSource.Util.bytes(salt),
				getName());
	}

	/**
	 * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用.
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(
			PrincipalCollection principals) {
		ShiroUser shiroUser = (ShiroUser) principals.getPrimaryPrincipal();
		User user = userService.findByLoginName(shiroUser.getLoginName(), true);
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		Set<String> permissions = new HashSet<>();

		if (user.isSuperAdmin()) { // 超级用户
			info.addRoles(roleService.findAllRoleNameByEnabled(true));
			List<String> perms = resourceService.findAllPermission();
			permissions.addAll(SproutStringUtils.getValue(perms, PERMS));
			permissions.addAll(perms);
		} else {
			Set<String> perms = new HashSet<String>();
			for (Role role : user.getRoles()) {
				if (role.getEnabled()) {
					info.addRole(role.getCode());
					perms.addAll(role.getAllPermissons());
				}
			}
			permissions.addAll(perms); //添加用户所有资源权限
		}
		info.addStringPermissions(permissions);
		return info;
	}

	/**
	 * 设定Password校验的Hash算法与迭代次数.
	 */
    @PostConstruct
	public void initCredentialsMatcher() {
		HashedCredentialsMatcher matcher = new HashedCredentialsMatcher(
				UserService.HASH_ALGORITHM);
		matcher.setHashIterations(UserService.HASH_INTERATIONS);
		setCredentialsMatcher(matcher);
	}

    private List<String> defaultPermissions;

	public List<String> getDefaultPermissions() {
		return defaultPermissions;
	}

	public void setDefaultPermissions(List<String> defaultPermissions) {
		this.defaultPermissions = defaultPermissions;
	}
    
}
