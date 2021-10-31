package com.sprout.core.service;

/**
 * Service层公用的Exception. Spring默认对RuntimeException类型的异常进行事务回滚
 * 
 * 从由Spring管理事务的函数中抛出时会触发事务回滚.
 * 
 * @author sofar
 */
public class SproutServiceException extends Exception {

	private static final long serialVersionUID = 1L;

	public SproutServiceException() {
		super();
	}

	public SproutServiceException(String message) {
		super(message);
	}

	public SproutServiceException(Throwable cause) {
		super(cause);
	}

	public SproutServiceException(String message, Throwable cause) {
		super(message, cause);
	}
}
