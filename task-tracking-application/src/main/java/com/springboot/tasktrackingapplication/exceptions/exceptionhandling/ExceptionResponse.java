package com.springboot.tasktrackingapplication.exceptions.exceptionhandling;

import lombok.Data;

@Data
public class ExceptionResponse {
    private int status;
    private String error;
    private String message;
    
    public ExceptionResponse(int status, String error, String message) {
        this.status = status;
        this.error = error;
        this.message = message;
    }
}
