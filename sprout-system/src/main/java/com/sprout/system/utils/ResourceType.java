package com.sprout.system.utils;

/**
 * 资源类型枚举类
 * @author Sofar
 *
 */
public enum ResourceType {

	/**
	 * 菜单资源
	 */
	MENU("菜单资源"),
	/**
	 * 操作资源
	 */
	ACTION("操作资源");

	private String typeName;
	
	ResourceType(String typeName) {
		this.typeName = typeName;
	}

	public String getTypeName() {
		return typeName;
	}

}
