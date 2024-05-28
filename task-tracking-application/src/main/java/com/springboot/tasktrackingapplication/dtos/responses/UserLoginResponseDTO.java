package com.springboot.tasktrackingapplication.dtos.responses;

import java.util.List;
import java.util.stream.Collectors;

import com.springboot.tasktrackingapplication.entity.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginResponseDTO {
	private Long id;
	private String username;
	private List<String> authorities;
	
	public UserLoginResponseDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
		this.authorities = user.getAuthorities().stream()
                						.map(authority -> authority.getName())
                						.collect(Collectors.toList());
	}
}
