package com.springboot.tasktrackingapplication.services;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.tasktrackingapplication.converters.TaskConverter;
import com.springboot.tasktrackingapplication.dtos.requests.TaskRequestDTO;
import com.springboot.tasktrackingapplication.dtos.requests.UpdateTaskDetailsRequestDTO;
import com.springboot.tasktrackingapplication.dtos.responses.TaskResponseDTO;
import com.springboot.tasktrackingapplication.entity.Status;
import com.springboot.tasktrackingapplication.entity.Task;
import com.springboot.tasktrackingapplication.entity.User;
import com.springboot.tasktrackingapplication.exceptions.NameNotFoundException;
import com.springboot.tasktrackingapplication.exceptions.TaskCreationException;
import com.springboot.tasktrackingapplication.exceptions.TaskRetrievalException;
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
	
    public User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("principal" + principal.toString());
        String username = ((UserDetails) principal).getUsername();
        return userRepository.findByUsername(username);
    }
    
	public List<TaskResponseDTO> getAllTasks() {
		log.info("Fetching all the Tasks");
		List<Task> allTasks = taskRepository.findallTasks();
		log.info("All tasks- " + allTasks);
		List<TaskResponseDTO> allTaskList = allTasks.stream()
												.map((allTask) -> taskConverter.mapToResponseDTO(allTask))
												.collect(Collectors.toList());
		return allTaskList;
	}
	
	public List<TaskResponseDTO> getAllTasksByUser(String username) {
		User authUser = getCurrentUser();
		if(authUser != null) {
			String authUsername = authUser.getUsername();
			String authority = authUser.getAuthorities().getFirst().getAuthority();
			log.info("username {} - {}", authUsername, authority);
			if(authUsername.equalsIgnoreCase(username) 
					|| authority.equalsIgnoreCase("ADMIN")) {
				User userByUsername = userRepository.findByUsername(username);
	    		if(userByUsername != null) {
					log.info("Fetching all the Tasks By User " + username);
					List<Task> allTasksByUser = taskRepository.findTasksByUsername(username);
					log.info("All tasks By user {} - {} ", username, allTasksByUser);
					List<TaskResponseDTO> allTaskList = allTasksByUser.stream()
															.map((allTask) -> taskConverter.mapToResponseDTO(allTask))
															.collect(Collectors.toList());
					return allTaskList;
	    		}
	    		throw new NameNotFoundException(HttpStatus.NOT_FOUND, "Username given not found");
			}
			throw new TaskRetrievalException(HttpStatus.FORBIDDEN, "This User cannot view Task status");
	    }
	    throw new TaskCreationException(HttpStatus.BAD_REQUEST, "User Not Authenticated");
	}
	
	public List<Task> getTaskStatus(String task) {
		List<Task> viewTask = taskRepository.findUsersByTask(task);
		log.info("View Task" + viewTask);
		return viewTask;
	}
    
	@Transactional
	public TaskResponseDTO saveTask(TaskRequestDTO taskRequest) {
        log.info("Saving Task");
		LocalDate today = LocalDate.now();
		if(taskRequest.getDueDate().isBefore(today)) {
			throw new TaskCreationException(HttpStatus.BAD_REQUEST, "Due Date past the task created date");
		}

		User userByUsername = userRepository.findByUsername(taskRequest.getUsername());
		if(userByUsername != null) {
			Task task = taskConverter.convertDtotoEntityCreated(taskRequest);

			// Initialize the users set if it is null
			if (task.getUser() == null) {
				task.setUser(userByUsername);
			}
			log.debug("task-" + task);
			taskRepository.save(task);


			TaskResponseDTO taskResponse = taskConverter.mapToResponseDTO(task);
			//taskResponse.setUsername(userByUsername.getUsername());

			return taskResponse;
		}
		throw new NameNotFoundException(HttpStatus.NOT_FOUND, "Username in request not found");
	}

	public TaskResponseDTO updateTask(UpdateTaskDetailsRequestDTO request) {
		User user = userRepository.findByUsername(request.getUsername());
		if(user != null) {
			Task updateTask = taskRepository.findTaskFromUserTasks(user.getUsername(), request.getTask());
			if(updateTask != null) {
				taskConverter.updateTaskDetails(request, updateTask);
				taskRepository.save(updateTask);
				
	        	TaskResponseDTO taskResponse = taskConverter.mapToResponseDTO(updateTask);
				return taskResponse;
			}
			throw new TaskRetrievalException(HttpStatus.NOT_FOUND, "Task not found for this User");
		}
		throw new NameNotFoundException(HttpStatus.NOT_FOUND, "Username in request not found");
	}
	
	public boolean deleteTask(Long taskId) {
		if(taskRepository.existsById(taskId)) {
			taskRepository.deleteById(taskId);
			return true;
		}
		return false;
	}
	
   @Transactional
   public void updatePastDueTasks() {
        LocalDate currentDate = LocalDate.now();
        List<Task> pastDueTasks = taskRepository.findPastDueTasks(currentDate);
        log.info("pastDueTasks - ", pastDueTasks);
        for (Task task : pastDueTasks) {
            task.setStatus(Status.DUE);
        }

        taskRepository.saveAll(pastDueTasks);
    }

}
