package com.springboot.tasktrackingapplication.dtos.requests;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskRequestDTO {
	
	private String username;
	private String task;
	private String description;
	private LocalDate dueDate;
	
}
