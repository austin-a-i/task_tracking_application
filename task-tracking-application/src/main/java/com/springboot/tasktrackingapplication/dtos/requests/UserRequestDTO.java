package com.springboot.tasktrackingapplication.dtos.requests;

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

}
