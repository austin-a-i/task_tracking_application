package com.springboot.tasktrackingapplication.dtos.responses;

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
public class TaskResponseDTO {
	
	private long taskId;
	private String task;
	private String description;
	private LocalDate dueDate;
	private Status status;
	
}
