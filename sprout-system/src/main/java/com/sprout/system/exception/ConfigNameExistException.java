package com.sprout.system.exception;

import com.sprout.core.service.SproutServiceException;

/**
 * 系统配置名称已存在异常定义类
 * @author sofar
 *
 */
public class ConfigNameExistException extends SproutServiceException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ConfigNameExistException() {
		super();
	}

	public ConfigNameExistException(String message) {
		super(message);
	}

}
