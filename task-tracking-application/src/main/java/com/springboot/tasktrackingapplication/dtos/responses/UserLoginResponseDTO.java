package com.springboot.tasktrackingapplication.dtos.responses;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginResponseDTO {
	    private String username;
	    private List<String> roles;
}
