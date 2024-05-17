package com.springboot.tasktrackingapplication.config;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import com.springboot.tasktrackingapplication.services.UserService;


@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Autowired
	private UserService userDetailsService;
	
	@Bean
	public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
	    return http.getSharedObject(AuthenticationManagerBuilder.class)
	            .userDetailsService(userDetailsService)
	            .and()
	            .build();
	}

	@Bean 
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception { 
		http.authorizeHttpRequests((authz) -> authz
				  .requestMatchers("/user/register","/user/login").permitAll()
				  .anyRequest().authenticated() ) 
		  		.httpBasic(withDefaults()); 
			// Disable CSRF - For POST,PUT
		http.csrf(csrf -> csrf.disable());
		return http.build(); 
	}

}
