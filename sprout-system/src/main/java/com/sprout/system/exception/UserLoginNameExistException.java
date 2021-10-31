package com.sprout.system.exception;

import com.sprout.core.service.SproutServiceException;

/**
 * 用户登录名已存在异常
 * 
 * @author sofar
 *
 */
public class UserLoginNameExistException extends SproutServiceException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UserLoginNameExistException() {
		super();
	}

	public UserLoginNameExistException(String message) {
		super(message);
	}

}
