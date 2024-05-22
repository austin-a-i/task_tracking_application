package com.springboot.tasktrackingapplication.dtos.responses;

import java.time.LocalDate;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.springboot.tasktrackingapplication.entity.Status;
import com.springboot.tasktrackingapplication.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskResponseDTO {
	
	private long taskId;
	@JsonIgnoreProperties({ "email", "password" })
	private Set<User> user;
	private String task;
	private String description;
	private LocalDate dueDate;
	private Status status;
	
}
