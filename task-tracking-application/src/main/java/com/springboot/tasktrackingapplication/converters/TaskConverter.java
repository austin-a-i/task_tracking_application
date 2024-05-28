package com.springboot.tasktrackingapplication.converters;

import org.springframework.stereotype.Component;

import com.springboot.tasktrackingapplication.dtos.requests.TaskRequestDTO;
import com.springboot.tasktrackingapplication.dtos.requests.UpdateTaskDetailsRequestDTO;
import com.springboot.tasktrackingapplication.dtos.responses.TaskResponseDTO;
import com.springboot.tasktrackingapplication.entity.Status;
import com.springboot.tasktrackingapplication.entity.Task;

@Component
public class TaskConverter {
 
	public Task convertDtotoEntityCreated(TaskRequestDTO request) {
		Task taskResponse;
		if(request.getStatus() != null) {
			taskResponse = Task.builder()
					.task(request.getTask())
					.description(request.getDescription())
					.dueDate(request.getDueDate())
					.status(request.getStatus())
					.build();
		} else {
			taskResponse = Task.builder()
					.task(request.getTask())
					.description(request.getDescription())
					.dueDate(request.getDueDate())
					.status(Status.IN_PROGRESS)
					.build();
		}
		return taskResponse;
	}
	
	public void updateTaskDetails(UpdateTaskDetailsRequestDTO request, Task updateTask) {
		updateTask.setDescription(request.getDescription());
		updateTask.setDueDate(request.getDueDate());
		updateTask.setStatus(request.getStatus());
	}

	public TaskResponseDTO mapToResponseDTO(Task savedTask) {
	    
		return TaskResponseDTO.builder()
							.taskId(savedTask.getId())
							.username(savedTask.getUser().getUsername())
							.task(savedTask.getTask())
							.description(savedTask.getDescription())
							.dueDate(savedTask.getDueDate())
							.status(savedTask.getStatus())
							.build();
	}

}
