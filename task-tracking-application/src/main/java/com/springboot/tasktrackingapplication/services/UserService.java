package com.springboot.tasktrackingapplication.services;

import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.springboot.tasktrackingapplication.converters.UserConverter;
import com.springboot.tasktrackingapplication.dtos.requests.UserRequestDTO;
import com.springboot.tasktrackingapplication.entity.User;
import com.springboot.tasktrackingapplication.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService implements UserDetailsService{
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserConverter userConverter;

	public String addUser(UserRequestDTO userRequest) {
		log.info("Request {}", userRequest.toString());
		User user = userConverter.convertDtotoEntity(userRequest);
		userRepository.save(user);	
		return "User " + user.getUsername() + " has been registered successfully ";
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User>user = findByUsername(username);
        if(user.isPresent()){
            User newUser=user.get();
            return new org.springframework.security.core.userdetails.User(
                    newUser.getUsername(),
                    newUser.getPassword(),
                    newUser.getRoles().stream().map((role)->new SimpleGrantedAuthority(role))
                    .collect(Collectors.toList())
            );

        }
        throw new UsernameNotFoundException("User not found");
	}

	private Optional<User> findByUsername(String username) {
        log.info("Getting user with the username " + username);
        return userRepository.findByUsername(username);
	}
	
	

}
