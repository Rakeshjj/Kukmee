package com.kukmee.exception;

public class ResourceNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String message;

	public ResourceNotFoundException(String message) {
		super();
		this.message = message;
	}

}
