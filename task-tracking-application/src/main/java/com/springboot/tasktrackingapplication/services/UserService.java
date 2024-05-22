package com.springboot.tasktrackingapplication.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.springboot.tasktrackingapplication.converters.UserConverter;
import com.springboot.tasktrackingapplication.dtos.requests.UserLoginRequestDTO;
import com.springboot.tasktrackingapplication.dtos.requests.UserRequestDTO;
import com.springboot.tasktrackingapplication.dtos.responses.UserLoginResponseDTO;
import com.springboot.tasktrackingapplication.entity.User;
import com.springboot.tasktrackingapplication.exceptions.ApiRequestException;
import com.springboot.tasktrackingapplication.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserConverter userConverter;
	
    @Autowired
    private AuthenticationManager authenticationManager;
	
	public List<UserLoginResponseDTO> getAllUsers() {
		log.info("Fetching all the Users");
		List<User> userList = userRepository.findAll();
		
		return userList.stream()
					.map((user)-> userConverter.mapToDTO(user))
					.collect(Collectors.toList());
	}

	public ResponseEntity<String> addUser(UserRequestDTO userRequest) {
		log.info("Request {}", userRequest.toString());
        User existingUser = userRepository.findByUsername(userRequest.getUsername());
        if (existingUser != null) {
            return ResponseEntity.badRequest().body("Username is already taken");
        }
        
		User user = userConverter.convertDtotoEntity(userRequest);
		
		userRepository.save(user);	
		return ResponseEntity.ok("User " + user.getUsername() + " has been registered successfully");
	}
	
	
	public UserLoginResponseDTO login(UserLoginRequestDTO request) throws ApiRequestException {
		Authentication auth;
		try {
			UsernamePasswordAuthenticationToken token = 
									new UsernamePasswordAuthenticationToken(request.getUsername()
	        																,request.getPassword());
	        log.info(request.getUsername()+":"+request.getPassword());
	        auth = authenticationManager.authenticate(token);
	        log.info(auth + " value");
			
		} catch (BadCredentialsException ex) {
            throw new ApiRequestException("Credentials are not valid!");
        }
		
		// Insert username and password into context
        SecurityContextHolder.getContext().setAuthentication(auth);
        User user = (User) auth.getPrincipal();
        
		List<GrantedAuthority>roles = (List<GrantedAuthority>)auth.getAuthorities();
        List<String>rolesResponse = roles.stream().map((authority)->authority.getAuthority())
        										  .collect(Collectors.toList());
        log.info("rolesResponse: " + rolesResponse.toString());
        UserLoginResponseDTO response = new UserLoginResponseDTO(user.getId(), 
        													user.getUsername(), rolesResponse);
        log.info("Response: " + response.toString());
        
		return response;

	}
	
    public UserLoginResponseDTO findByUsername(String username) throws ApiRequestException {
        try {
            User user = userRepository.findByUsername(username);
            return new UserLoginResponseDTO(user);
        } catch (UsernameNotFoundException e) {
            throw new ApiRequestException("User with username '" + username + "' doesn't exist.");
        }
    }

}
