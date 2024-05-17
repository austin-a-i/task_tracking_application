package com.springboot.tasktrackingapplication.services;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.springboot.tasktrackingapplication.converters.TaskConverter;
import com.springboot.tasktrackingapplication.dtos.requests.TaskRequestDTO;
import com.springboot.tasktrackingapplication.dtos.responses.TaskResponseDTO;
import com.springboot.tasktrackingapplication.entity.Task;
import com.springboot.tasktrackingapplication.entity.User;
import com.springboot.tasktrackingapplication.exceptions.TaskException;
import com.springboot.tasktrackingapplication.repository.TaskRepository;
import com.springboot.tasktrackingapplication.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TaskService {
	
	@Autowired
	private TaskRepository taskRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private TaskConverter taskConverter;

	public TaskResponseDTO saveTask(TaskRequestDTO taskRequest) {
        log.info("Saving Task");
        Optional<User> user = userRepository.findByUsername(taskRequest.getUsername());
        
        if(user.isPresent()) {
    		LocalDate today = LocalDate.now();
    		if(taskRequest.getDueDate().isAfter(today)) {
    			throw new TaskException(HttpStatus.BAD_REQUEST, "Due Date past the task created date");
    		}
    		
        	Task task = taskConverter.convertDtotoEntity(user, taskRequest);
        	Task savedTask = taskRepository.save(task);
        	TaskResponseDTO taskResponse = taskConverter.mapToResponseDTO(savedTask);
        	return taskResponse;
        }
        
        throw new TaskException(HttpStatus.BAD_REQUEST, "User not present");
	}
	
	

}
