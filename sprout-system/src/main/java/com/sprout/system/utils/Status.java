package com.sprout.system.utils;

/**
 * 状态枚举类
 * 
 * @author Sofar
 * 
 */
public enum Status {

	ENABLE("启用"),
	DISABLE("禁用"),
	LOCK("锁定");
	
	private String statusName;

	Status(String statusName) {
		this.statusName = statusName;
	}

	public String getStatusName() {
		return statusName;
	}

}
