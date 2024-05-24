package com.springboot.tasktrackingapplication.dtos.requests;

import java.time.LocalDate;

import com.springboot.tasktrackingapplication.entity.Status;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateTaskDetailsRequestDTO {
	
	private String username;
	/*
	 task shouldn't change since we are looking into repository using task name 
		and if changed will throw a TaskRetrievalException stating no such Task Found
	*/
	private String task;
	private String description;
	private LocalDate dueDate;
	private Status status;
	
}
