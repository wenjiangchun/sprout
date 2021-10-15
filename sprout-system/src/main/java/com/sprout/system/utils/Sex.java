package com.sprout.system.utils;

/**
 * 性别 枚举类  
 * @author Sofar
 *
 */
public enum Sex {

	/**
	 * 男
	 */
	M("男"), 
	
	/**
	 * 女
	 */
	F("女");
	

	private String sexName;

	private Sex(String sexName) {
		this.sexName = sexName;
	}

	public String getSexName() {
		return sexName;
	}

	public void setSexName(String sexName) {
		this.sexName = sexName;
	}

}
