package com.sprout.system.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sprout.core.jpa.entity.AbstractBaseEntity;
import com.sprout.system.utils.Status;

import javax.persistence.*;
import java.util.*;

/**
 * 系统组织机构实体类
 *
 * @author sofar
 */
@Entity
//@Table(name = "SYS_GROUP", uniqueConstraints = {@UniqueConstraint(columnNames = {"PARENT_ID", "CODE"})})
@Table(name = "sys_group")
@JsonIgnoreProperties(value = {"childs", "roles", "users"})
public class Group extends AbstractBaseEntity<Long> {

    private static final long serialVersionUID = 1L;

    /**
     * 机构类型对应字典表中跟字典代码名称，当需要给机构添加机构类型时，首先在字典表中添加字典代码为"{@code GROUP_TYPE}"的
     * 跟字典对象，然后在该字典对象下添加需要的机构类型字典对象，当使用时则通过跟字典对象"{@code GROUP_TYPE}"去查找下面
     * 所有机构类型子字典对象。
     */
    public static final String GROUP_TYPE = "GROUP_TYPE";

    /**
     * 机构名称
     */
    private String name;

    /**
     * 机构全称
     */
    private String fullName;

    /**
     * 上级机构
     */
    private Group parent;

    /**
     * 子机构
     */
    private Set<Group> childs = new HashSet<>();

    /**
     * 机构下用户
     */
    private Set<User> users = new HashSet<>();

    /**
     * 机构角色组
     */
    private Set<Role> roles = new HashSet<>();

    /**
     * 机构代码
     */
    private String code;

    /**
     * 机构类型
     */
    private Dict groupType;

    /**
     * 机构地址
     */
    private String address;

    /**
     * 机构电话
     */
    private String tel;

    /**
     * 机构备注
     */
    private String description;

    private String linker;
    private String linkerMobile;
    /**
     * 机构状态
     */
    private Status status = Status.ENABLE;

    @Column(unique = true, length = 100)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(unique = true, length = 200)
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @ManyToOne
    @JoinColumn(name = "parent_id")
    public Group getParent() {
        return parent;
    }

    public void setParent(Group parent) {
        this.parent = parent;
    }

    @OneToMany(mappedBy = "parent")
    public Set<Group> getChilds() {
        return childs;
    }

    public void setChilds(Set<Group> childs) {
        this.childs = childs;
    }

    @OneToMany(mappedBy = "group")
    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    @ManyToMany
    @JoinTable(
            name = "sys_group_role"
            , joinColumns = {
            @JoinColumn(name = "group_id")
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

    @Column(unique = true, length = 20)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @ManyToOne
    @JoinColumn(name = "type_id")
    public Dict getGroupType() {
        return groupType;
    }

    public void setGroupType(Dict groupType) {
        this.groupType = groupType;
    }

    @Column(length = 200)
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Column(length = 15)
    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    @Column(length = 200)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLinker() {
        return linker;
    }

    public void setLinker(String linker) {
        this.linker = linker;
    }

    public String getLinkerMobile() {
        return linkerMobile;
    }

    public void setLinkerMobile(String linkerMobile) {
        this.linkerMobile = linkerMobile;
    }

    /**
     * 对组增加用户
     *
     * @param user 用户对象
     */
    public void addUser(User user) {
        user.setGroup(this);
    }

    /**
     * 对机构增加角色
     *
     * @param role 角色对象
     * @return 当前机构所有角色
     */
    public Set<Role> addRole(Role role) {
        this.roles.add(role);
        return this.roles;
    }

    /**
     * 删除机构角色
     *
     * @param role 角色对象
     * @return 当前机构所有角色
     */
    public Set<Role> removeRole(Role role) {
        this.roles.remove(role);
        return this.roles;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    private Long pid;

    @Transient
    public Long getPid() {
        return parent != null ? parent.getId() : null;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    @Override
    public String toString() {
        return "Group [name=" + name + ", fullName=" + fullName + ", code="
                + code + ", address=" + address + ", tel=" + tel + "]";
    }


    /**
     * 获取机构所在顶级机构信息 如果机构本身为顶级机构则回返自己
     * @return 机构所在顶级机构
     */
    @JsonIgnore
    @Transient
    public Group getRootGroup() {
        if (this.getParent() == null) {
            return this;
        } else {
            return this.getParent().getRootGroup();
        }
    }

    /**
     * 获取机构所在顶级机构信息 如果机构本身为顶级机构则回返自己
     * @return 机构所在顶级机构
     */
    @JsonIgnore
    @Transient
    public List<Group> getChildList(Status status) {
        List<Group> childList = new ArrayList<>();
        for (Group child : this.getChilds()) {
            if (child.getStatus() == status) {
                childList.add(child);
                childList.addAll(child.getChildList(status));
            }
        }
        return childList;
    }
}
