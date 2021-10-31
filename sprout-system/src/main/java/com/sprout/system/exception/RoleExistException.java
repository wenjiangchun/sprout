package com.sprout.system.exception;

import com.sprout.core.service.SproutServiceException;

/**
 * 角色已存在异常类
 * @author Sofar
 *
 */
public class RoleExistException extends SproutServiceException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RoleExistException() {
		super();
	}
	
	public RoleExistException(String message) {
		super(message);
	}
}
