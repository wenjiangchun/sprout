package com.sprout.system.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sprout.core.jpa.entity.AbstractBaseEntity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * 系统字典实体类
 * 
 * @author Sofar
 */
@Entity
@Table(name="sys_dict",uniqueConstraints={@UniqueConstraint(columnNames={"parent_id","code"})})
@JsonIgnoreProperties(value = {"childs"})
public class Dict extends AbstractBaseEntity<Long> {
	
	private static final long serialVersionUID = 1L;

	/**
	 * 字典名称
	 */
	private String name;
	
	/**
	 * 字典代码
	 */
	private String code;
	
	/**
	 * 所属字典分类
	 */
	private Dict parent;
	
	/**
	 * 所有字典子集合
	 */
	private Set<Dict> childs = new HashSet<Dict>();

	/**
	 * 是否启用
	 */
	private Boolean enabled = true;
	
	/**
	 * 字典说明
	 */
	private String description;

	private Integer sn = 1;

	@Column(length = 20)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(length = 20)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@ManyToOne
	@JoinColumn(name="parent_id")
	public Dict getParent() {
		return parent;
	}

	public void setParent(Dict parent) {
		this.parent = parent;
	}

	@OneToMany(mappedBy="parent")
	@OrderBy("sn ASC")
	public Set<Dict> getChilds() {
		return childs;
	}

	public void setChilds(Set<Dict> childs) {
		this.childs = childs;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	@Column(length = 300)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getSn() {
		return sn;
	}

	public void setSn(Integer sn) {
		this.sn = sn;
	}

	@Override
	public String toString() {
		return "Dictionary [name=" + name + ", code=" + code + ", parent="
				+ parent + ", isEnabled=" + enabled + ", description="
				+ description + "]";
	}

	
	private Long pid;

	@Transient
	public Long getPid() {
		return parent != null ? parent.getId() : null;
	}

	public void setPid(Long pid) {
		this.pid = pid;
	}

}