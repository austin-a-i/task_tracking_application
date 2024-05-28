package com.springboot.tasktrackingapplication.exceptions;

public class ApiRequestException extends RuntimeException {

	public ApiRequestException(String message) {
		super(message);
	}

}
