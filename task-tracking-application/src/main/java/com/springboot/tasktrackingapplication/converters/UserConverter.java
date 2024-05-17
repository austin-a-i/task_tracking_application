package com.springboot.tasktrackingapplication.converters;

import org.springframework.stereotype.Component;

import com.springboot.tasktrackingapplication.dtos.requests.UserRequestDTO;
import com.springboot.tasktrackingapplication.entity.User;
import com.springboot.tasktrackingapplication.exceptions.NameNotFoundException;

@Component
public class UserConverter {

	public User convertDtotoEntity(UserRequestDTO userRequest){ 
		if(userRequest.getName() != null) {
			 User userResponse = new User(userRequest.getName(), userRequest.getEmail()
																	, userRequest.getPassword());

			 return userResponse;
		}
		throw new NameNotFoundException("UserName is null/not found - Cannot convert to Entity User");
	}

}
