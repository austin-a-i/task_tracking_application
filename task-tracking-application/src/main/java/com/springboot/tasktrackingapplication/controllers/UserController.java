package com.springboot.tasktrackingapplication.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.tasktrackingapplication.dtos.requests.UserLoginRequestDTO;
import com.springboot.tasktrackingapplication.dtos.requests.UserRequestDTO;
import com.springboot.tasktrackingapplication.dtos.responses.UserLoginResponseDTO;
import com.springboot.tasktrackingapplication.entity.User;
import com.springboot.tasktrackingapplication.services.UserDetailsServiceImpl;
import com.springboot.tasktrackingapplication.services.UserService;

import lombok.extern.slf4j.Slf4j;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {
    
	@Autowired
	private UserService userService;
	
    @GetMapping("/hello")
    public ResponseEntity<String> test(Authentication authentication){
        return new ResponseEntity<>("Hello",HttpStatus.OK);
    }
	
	@GetMapping("/all")
	public ResponseEntity<List<UserLoginResponseDTO>> getAllUsers() {
		List<UserLoginResponseDTO> userList = userService.getAllUsers();
		return new ResponseEntity<>(userList, HttpStatus.OK);
		
	}
	
	@PostMapping("/register")
	public ResponseEntity<String> registerUser(@RequestBody UserRequestDTO user) {
		ResponseEntity<String> registeredUser = userService.addUser(user);
		return registeredUser;
	}
	
    @PostMapping("/login")
    public ResponseEntity<UserLoginResponseDTO> login(@RequestBody UserLoginRequestDTO request){
        return new ResponseEntity<>(userService.login(request), HttpStatus.OK);
    }

}
