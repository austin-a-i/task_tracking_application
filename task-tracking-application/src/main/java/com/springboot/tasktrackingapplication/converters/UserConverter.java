package com.springboot.tasktrackingapplication.converters;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.springboot.tasktrackingapplication.dtos.requests.UserRequestDTO;
import com.springboot.tasktrackingapplication.dtos.responses.UserLoginResponseDTO;
import com.springboot.tasktrackingapplication.entity.Authority;
import com.springboot.tasktrackingapplication.entity.User;
import com.springboot.tasktrackingapplication.exceptions.NameNotFoundException;

@Component
public class UserConverter {
	
	@Autowired
    private PasswordEncoder passwordEncoder;

	public User convertDtotoEntity(UserRequestDTO userRequest) {
		User userResponse;
		if(userRequest.getUsername() != null) {
			if(! userRequest.getAuthorities().isEmpty()) {
				userResponse = new User(userRequest.getUsername(), userRequest.getEmail()
						, passwordEncoder.encode(userRequest.getPassword()), userRequest.isEnabled()
						, userRequest.getAuthorities());
						 
			} else {
				userResponse = new User(userRequest.getUsername(), userRequest.getEmail()
						, passwordEncoder.encode(userRequest.getPassword()));
				userResponse.setEnabled(true);
				List<Authority> authorities = setUserAuthority();
				userResponse.setAuthorities(authorities);
					
			}
			return userResponse;
			 
			
		}
		throw new NameNotFoundException("UserName is null/not found - Cannot convert to Entity User");
	}

	public List<Authority> setUserAuthority() {
		Authority userAuthority = Authority.builder().name("User").build();
		List<Authority> authorities = new ArrayList<>();
		authorities.add(userAuthority);
		return authorities;
	}

	public UserLoginResponseDTO mapToDTO(User user) {
		// TODO Auto-generated method stub
		return null;
	}

}
