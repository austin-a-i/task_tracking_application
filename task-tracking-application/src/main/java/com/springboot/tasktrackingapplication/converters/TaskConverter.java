package com.springboot.tasktrackingapplication.converters;

import java.util.Set;

import org.springframework.stereotype.Component;

import com.springboot.tasktrackingapplication.dtos.requests.TaskRequestDTO;
import com.springboot.tasktrackingapplication.dtos.responses.TaskResponseDTO;
import com.springboot.tasktrackingapplication.entity.Status;
import com.springboot.tasktrackingapplication.entity.Task;
import com.springboot.tasktrackingapplication.entity.User;

@Component
public class TaskConverter {
 
	public Task convertDtotoEntity(TaskRequestDTO request) {
		
		Task taskResponse = Task.builder()
								.task(request.getTask())
								.description(request.getDescription())
								.dueDate(request.getDueDate())
								.status(Status.IN_PROGRESS)
								.build();
	    
		return taskResponse;
	}

	public TaskResponseDTO mapToResponseDTO(Task savedTask) {
		
		return TaskResponseDTO.builder()
							.taskId(savedTask.getId())
							.user(savedTask.getUser())
							.task(savedTask.getTask())
							.description(savedTask.getDescription())
							.dueDate(savedTask.getDueDate())
							.status(savedTask.getStatus())
							.build();
	}
}
