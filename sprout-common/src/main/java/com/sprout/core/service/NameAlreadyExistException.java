package com.sprout.core.service;

/**
 * 对于名称唯一的数据，当插入或者更新名称重复时抛出该异常
 */
public class NameAlreadyExistException extends HazeServiceException {

	private static final long serialVersionUID = 1L;

	public NameAlreadyExistException() {
	}

	public NameAlreadyExistException(String message) {
		super(message);
	}

	public NameAlreadyExistException(Throwable cause) {
		super(cause);
	}

	public NameAlreadyExistException(String message, Throwable cause) {
		super(message, cause);
	}
}
