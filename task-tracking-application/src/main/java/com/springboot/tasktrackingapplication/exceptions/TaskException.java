package com.springboot.tasktrackingapplication.exceptions;

import org.springframework.http.HttpStatus;

public class TaskException extends RuntimeException {

	public TaskException(HttpStatus httpStatus, String message) {
		
	}
}
