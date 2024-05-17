package com.springboot.tasktrackingapplication.dtos.requests;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserLoginRequestDTO{
    private String username;
    private String password;
}
