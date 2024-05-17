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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.tasktrackingapplication.dtos.requests.UserLoginRequestDTO;
import com.springboot.tasktrackingapplication.dtos.requests.UserRequestDTO;
import com.springboot.tasktrackingapplication.dtos.responses.UserLoginResponseDTO;
import com.springboot.tasktrackingapplication.services.UserService;

import lombok.extern.slf4j.Slf4j;

@CrossOrigin
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
	
    @Autowired
    private AuthenticationManager authenticationManager;
    
	@Autowired
	private UserService userService;
	
	@PostMapping("/register")
	public String registerUser(@RequestBody UserRequestDTO user) {
		String registerUser = userService.addUser(user);
		return registerUser;
	}
	
    @PostMapping("/login")
    public ResponseEntity<UserLoginResponseDTO> login(@RequestBody UserLoginRequestDTO request){
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(request.getUsername()
        																						,request.getPassword());
        log.info(request.getUsername()+":"+request.getPassword());
        Authentication auth=authenticationManager.authenticate(token);
        List<GrantedAuthority>roles=(List<GrantedAuthority>)auth.getAuthorities();
        List<String>rolesResponse=roles.stream().map((authority)->authority.getAuthority()).collect(Collectors.toList());
        UserLoginResponseDTO response=new UserLoginResponseDTO(auth.getName(),rolesResponse);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
