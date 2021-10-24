package com.sprout.system.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sprout.common.util.SproutStringUtils;
import com.sprout.core.jpa.entity.AbstractBaseEntity;
import com.sprout.system.utils.Sex;
import com.sprout.system.utils.Status;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * 系统用户信息实体类
 *
 * @author sofar
 */
@Entity
@Table(name = "sys_user")
@JsonIgnoreProperties(value = {"roles"})
public class User extends AbstractBaseEntity<Long> {

    /**
     * 超级管理员用户登录名。
     */
    public static final String ADMIN = "admin";

    /**
     * 用户默认密码,当新添加或重置用户时使用该密码。
     */
    public static final String DEFAULT_PASSWORD = "123456";

    /**
     * 登录名
     */

    private String loginName;

    /**
     * 姓名
     */
    private String name;

    /**
     * 登录密码
     */
    private String password;

    private Sex sex = Sex.M;

    private String email;

    private String mobile;

    private String tel;

    /**
     * 用户状态
     */
    private Status status = Status.ENABLE;


    private String salt;

    /**
     * 用户角色组
     */
    private Set<Role> roles = new HashSet<>();

    private Group group;

    private String description;

    public User() {
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(length = 1)
    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    @Column(length = 50)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(length = 1)
    @Enumerated(EnumType.ORDINAL)
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Column(unique = true, length = 20)
    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    @Column(length = 20)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    @Column(length = 11)
    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Column(length = 15)
    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    @ManyToMany
    @JoinTable(
            name = "sys_user_role"
            , joinColumns = {
            @JoinColumn(name = "user_id")
    }
            , inverseJoinColumns = {
            @JoinColumn(name = "role_id")
    }
    )
    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "group_id")
    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    /**
     * 对用户增加角色
     *
     * @param role 角色对象
     * @return 当前用户所有角色
     */
    public Set<Role> addRole(Role role) {
        this.roles.add(role);
        return this.roles;
    }

    /**
     * 删除用户角色
     *
     * @param role 角色对象
     * @return 当前用户所有角色
     */
    public Set<Role> removeRole(Role role) {
        this.roles.remove(role);
        return this.roles;
    }

    /**
     * 获取用户所有资源权限，包括用户角色所有权限和用户所在机构所有权限
     *
     * @return 用户当前所有资源权限
     */
    @Transient
    public Set<String> getAllPermission() {
        Set<String> permissions = new HashSet<String>();
        for (Role role : this.getRoles()) {
            //增加用户角色下所有资源权限
            permissions.addAll(role.getAllPermissons());
        }
        Group group = this.getGroup();
        if (group != null) {
            for (Role role : group.getRoles()) {
                //增加用户所在机构下所有资源权限
                permissions.addAll(role.getAllPermissons());
            }
        }
        return permissions;
    }

    /**
     * 获取用户所有角色名称 以逗号隔开
     *
     * @return 角色名称
     */
    @Transient
    public String getRoleNames() {
        String roleName = "";
        if (roles != null) {
            for (Role role : roles) {
                roleName += role.getName() + ",";
            }
        }
        return SproutStringUtils.removeEnd(roleName, ",");
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 判断用户是否为超级管理员 如果loginName为"admin"则为超级管理员
     *
     * @return true/false
     */
    @Transient
    public boolean isSuperAdmin() {
        return ADMIN.equals(this.getLoginName());
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", loginName='" + loginName + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", sex=" + sex +
                ", email='" + email + '\'' +
                ", mobile='" + mobile + '\'' +
                ", tel='" + tel + '\'' +
                ", status=" + status +
                ", salt='" + salt + '\'' +
                ", roles=" + roles +
                ", group=" + group +
                ", description='" + description + '\'' +
                ", entryDay=" + entryDay +
                ", workStartYear=" + workStartYear +
                ", workNum='" + workNum + '\'' +
                '}';
    }

    //入职日期
    private Date entryDay;

    //工作开始年份
    private Integer workStartYear;

    //工号
    private String workNum;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    public Date getEntryDay() {
        return entryDay;
    }

    public void setEntryDay(Date entryDay) {
        this.entryDay = entryDay;
    }

    @Column(length = 10)
    public Integer getWorkStartYear() {
        return workStartYear;
    }

    public void setWorkStartYear(Integer workStartYear) {
        this.workStartYear = workStartYear;
    }

    @Column(length = 10)
    public String getWorkNum() {
        return workNum;
    }

    public void setWorkNum(String workNum) {
        this.workNum = workNum;
    }
}