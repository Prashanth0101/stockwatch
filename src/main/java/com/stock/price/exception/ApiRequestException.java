package com.stock.price.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiRequestException extends Exception {

	private static final long serialVersionUID = 1L;

	private HttpStatus statusCode;

	public ApiRequestException() {
		super();
	}

	public ApiRequestException(HttpStatus code, String message) {
		super(message);
		this.statusCode = code;
	}

}
