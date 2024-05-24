package com.springboot.tasktrackingapplication.exceptions;

import org.springframework.http.HttpStatus;

public class TaskRetrievalException extends RuntimeException {

	public TaskRetrievalException(HttpStatus httpStatus, String message) {
		super(message);
	}
}
