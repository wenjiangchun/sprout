package com.sprout.system.exception;


import com.sprout.core.service.SproutServiceException;

/**
 * 配置删除失败异常
 * @author sofar
 *
 */
public class ConfigCannotDeleteException extends SproutServiceException {

	private static final long serialVersionUID = 1L;

	public ConfigCannotDeleteException() {
		super();
	}

	public ConfigCannotDeleteException(String message) {
		super(message);
	}
	
}
