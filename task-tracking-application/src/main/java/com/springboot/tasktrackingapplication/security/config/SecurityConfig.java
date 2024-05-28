package com.springboot.tasktrackingapplication.security.config;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.springboot.tasktrackingapplication.security.auth.RestAuthenticationEntryPoint;
import com.springboot.tasktrackingapplication.services.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
	
	@Autowired
	private UserDetailsServiceImpl userDetailsService;
	
    @Autowired
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;
	
	@Bean
	public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
	    return http.getSharedObject(AuthenticationManagerBuilder.class)
	            .userDetailsService(userDetailsService)
	            .passwordEncoder(passwordEncoder())
	            .and()
	            .build();
	}
	
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

	@Bean 
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception { 
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
        	.exceptionHandling().authenticationEntryPoint(restAuthenticationEntryPoint).and()
        	.authorizeHttpRequests((authz) -> 
        							authz.requestMatchers("/user/register","/user/login").permitAll()
        								.anyRequest().authenticated() ) 
		  	.httpBasic(withDefaults()); 
			// Disable CSRF - For POST,PUT
		http.csrf(csrf -> csrf.disable());
		return http.build(); 
	}
	
	/*
	 * .requestMatchers("/user/register","/user/login").permitAll()
	 * .anyRequest().authenticated()
	 */

}
