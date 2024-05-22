package com.springboot.tasktrackingapplication.services;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.tasktrackingapplication.converters.TaskConverter;
import com.springboot.tasktrackingapplication.dtos.requests.TaskRequestDTO;
import com.springboot.tasktrackingapplication.dtos.responses.TaskResponseDTO;
import com.springboot.tasktrackingapplication.entity.Task;
import com.springboot.tasktrackingapplication.entity.User;
import com.springboot.tasktrackingapplication.exceptions.NameNotFoundException;
import com.springboot.tasktrackingapplication.exceptions.TaskCreationException;
import com.springboot.tasktrackingapplication.repository.TaskRepository;
import com.springboot.tasktrackingapplication.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TaskService {
	
	@Autowired
	private TaskRepository taskRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private TaskConverter taskConverter;
	
    private User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("principal" + principal.toString());
        String username = ((UserDetails) principal).getUsername();
        return userRepository.findByUsername(username);
    }
    
	public List<TaskResponseDTO> getAllTasks() {
		log.info("Fetching all the Tasks");
		List<Task> allTasks = taskRepository.findAll();
		List<TaskResponseDTO> allTaskList = allTasks.stream()
												.map((allTask) -> taskConverter.mapToResponseDTO(allTask))
												.collect(Collectors.toList());
		return allTaskList;
	}

	public TaskResponseDTO getTaskStatus(String task) {
		User user = getCurrentUser();
		
		if(user != null) {
			Task viewTask = taskRepository.findByTaskAndUser(task, user.getUsername());
			TaskResponseDTO getTask = taskConverter.mapToResponseDTO(viewTask);
			return getTask;
		}
		throw new UsernameNotFoundException("No user found for the Task");
	}
    
	@Transactional
	public TaskResponseDTO saveTask(TaskRequestDTO taskRequest) {
        log.info("Saving Task");
        User authUser = getCurrentUser();
        
        if(authUser != null) {
    		LocalDate today = LocalDate.now();
    		if(! taskRequest.getDueDate().isAfter(today)) {
    			throw new TaskCreationException(HttpStatus.BAD_REQUEST, "Due Date past the task created date");
    		}
    		
    		User userByUsername = userRepository.findByUsername(taskRequest.getUsername());
    		if(userByUsername != null) {
    			Task task = taskConverter.convertDtotoEntity(taskRequest);

	    	    // Initialize the users set if it is null
	    	    if (task.getUser() == null) {
	    	    	task.setUser(new HashSet<>());
	    	    }
	    	    // Add the user to the task's users set
	    	    task.getUser().add(userByUsername);
	    	    
	        	Task savedTask = taskRepository.save(task);
        	    userRepository.save(userByUsername);
	        	
	        	TaskResponseDTO taskResponse = taskConverter.mapToResponseDTO(savedTask);
	        	
    	    
	        	/*
        	    // Initialize the user's tasks set if it is null
        	    if (userByUsername.getTasks() == null) {
        	    	userByUsername.setTasks(new HashSet<>());
        	    }
        	    
        	    // Add the task to the user's tasks set
        	    userByUsername.getTasks().add(task);
        	    */
        	    return taskResponse;
    	    }
    		throw new NameNotFoundException(HttpStatus.NOT_FOUND, "Username in request not found");
        	
        }
        throw new TaskCreationException(HttpStatus.BAD_REQUEST, "Not Authenticated");
	}

}
