package com.springboot.tasktrackingapplication.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.springboot.tasktrackingapplication.entity.User;
import com.springboot.tasktrackingapplication.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 */
@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Getting user with the username " + username);
        User user = userRepository.findByUsername(username);
        if(user != null){
            return user;
        }
        throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
	}
}
