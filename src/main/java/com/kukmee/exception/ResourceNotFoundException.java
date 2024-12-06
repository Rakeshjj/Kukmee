package com.kukmee.exception;


public class ResourceNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ResourceNotFoundException(String message) {
        super(message); // Use the message field from the superclass
    }
}

