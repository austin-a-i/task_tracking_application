package com.springboot.tasktrackingapplication.exceptions;

import org.springframework.http.HttpStatus;

public class TaskCreationException extends RuntimeException {

	public TaskCreationException(HttpStatus httpStatus, String message) {
		super(message);
	}
}
