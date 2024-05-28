package com.springboot.tasktrackingapplication.exceptions;

import org.springframework.http.HttpStatus;

public class NameNotFoundException extends RuntimeException {

	public NameNotFoundException(String message) {
		super(message);
	}

	public NameNotFoundException(HttpStatus notFound, String message) {
		super(message);
	}

}
