package com.springboot.tasktrackingapplication.dtos.requests;

import java.util.List;

import com.springboot.tasktrackingapplication.entity.Authority;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDTO {
	
	private String username;
	private String email;
	private String password;
    private boolean enabled;
    private List<Authority> authorities;

}
