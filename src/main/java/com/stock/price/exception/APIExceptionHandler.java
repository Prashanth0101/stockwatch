package com.stock.price.exception;

import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class APIExceptionHandler {
	
	@ExceptionHandler(value = { ApiRequestException.class })
	public ResponseEntity<Object> handleAPIRequestException(ApiRequestException e) {
		HttpStatus httpStatus = e.getStatusCode();
		APIException apiException = new APIException(httpStatus, e.getMessage());
		return ResponseEntity.status(httpStatus).body(apiException);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public final ResponseEntity<Object> handleConstraintViolationExceptions(ConstraintViolationException ex) {
		String exceptionResponse = String.format("Invalid input parameters: %s\n", ex.getMessage());
		return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);
	}


}
