package com.kukmee.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ErrorMessage> constarint(ConstraintViolationException e) {
		ErrorMessage errorMessage = new ErrorMessage(HttpStatus.BAD_REQUEST.value(), "Username cannot start number");

		return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<ErrorMessage> handleAuthenticationException(AuthenticationException e) {
		ErrorMessage errorMessage = new ErrorMessage(HttpStatus.UNAUTHORIZED.value(),
				"Full authentication is required to access this resource");
		return new ResponseEntity<>(errorMessage, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ErrorMessage> handleAccessDeniedException(AccessDeniedException e) {
		ErrorMessage errorMessage = new ErrorMessage(HttpStatus.FORBIDDEN.value(),
				"You do not have permission to access this resource");
		return new ResponseEntity<>(errorMessage, HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	}
	
	

}

//@ExceptionHandler(ConstraintViolationException.class)
//public ResponseEntity<ErrorMessage> handleConstraintViolationException(ConstraintViolationException e) {
//
//	Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
//
//	StringBuilder errorMessages = new StringBuilder();
//	for (ConstraintViolation<?> violation : violations) {
//		errorMessages.append(violation.getMessage()).append("; ");
//	}
//
//	ErrorMessage errorMessage = new ErrorMessage(HttpStatus.BAD_REQUEST.value(), errorMessages.toString());
//
//	return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
//}
