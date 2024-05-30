package com.springboot.tasktrackingapplication.security.auth;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.tasktrackingapplication.exceptions.exceptionhandling.ExceptionResponse;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /* This method is activated when unauthorized user tries to access secured REST service */
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
    	
    	log.info(authException.toString());
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(new ObjectMapper().writeValueAsString(
            new ExceptionResponse(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized", authException.getMessage())
        ));
    	
        //response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
    }
}