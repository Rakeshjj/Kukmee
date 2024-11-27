package com.kukmee.exception;

public class DuplicateValueExistsException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String message;

	public DuplicateValueExistsException(String message) {
		super();
		this.message = message;
	}

}
