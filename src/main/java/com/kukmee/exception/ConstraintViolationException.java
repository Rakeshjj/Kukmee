package com.kukmee.exception;

public class ConstraintViolationException extends RuntimeException {

	private String message;

	public ConstraintViolationException(String message) {
		super();
		this.message = message;
	}

}
