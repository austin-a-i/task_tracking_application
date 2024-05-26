package com.springboot.tasktrackingapplication.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.tasktrackingapplication.dtos.requests.TaskRequestDTO;
import com.springboot.tasktrackingapplication.dtos.requests.UpdateTaskDetailsRequestDTO;
import com.springboot.tasktrackingapplication.dtos.responses.TaskResponseDTO;
import com.springboot.tasktrackingapplication.entity.Task;
import com.springboot.tasktrackingapplication.exceptions.TaskCreationException;
import com.springboot.tasktrackingapplication.services.TaskService;


@RestController
@RequestMapping("/tasks")
public class TaskController {
	
	@Autowired
	private TaskService taskService;
	
	@GetMapping("/viewOne/{task}")
	public ResponseEntity<List<Task>> viewTaskStatus(@PathVariable String task) {
		List<Task> viewTask = taskService.getTaskStatus(task);
		return new ResponseEntity<>(viewTask, HttpStatus.OK);
	}
	
	@GetMapping("/viewAll")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<List<TaskResponseDTO>> viewAllTask() {
		List<TaskResponseDTO> taskList = taskService.getAllTasks();
		return new ResponseEntity<>(taskList, HttpStatus.OK);
	}
	
	@GetMapping("/view/user/{username}")
	public ResponseEntity<List<TaskResponseDTO>> viewAllTaskByUser(@PathVariable String username) {
		List<TaskResponseDTO> userTaskList = taskService.getAllTasksByUser(username);
		return new ResponseEntity<>(userTaskList, HttpStatus.OK);
	}
	
	@PostMapping("/create")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<TaskResponseDTO> createTask(@RequestBody TaskRequestDTO taskRequest) {
		try {
		TaskResponseDTO taskResponse = taskService.saveTask(taskRequest);
		return new ResponseEntity<>(taskResponse,HttpStatus.CREATED);
		} catch (DataIntegrityViolationException e) {
            throw new TaskCreationException(HttpStatus.CONFLICT, "Task name must be unique within a user");
		}
		
	}
	
	@PutMapping("/update")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<TaskResponseDTO> updateTask(@RequestBody UpdateTaskDetailsRequestDTO updateRequest) {
		return new ResponseEntity<>(taskService.updateTask(updateRequest), HttpStatus.PARTIAL_CONTENT);
	}
	
	@DeleteMapping("/delete/{taskId}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<String> deleteTask(@PathVariable Long taskId) {
		boolean deleteTask = taskService.deleteTask(taskId);
		if (!deleteTask) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found");
		}
		return ResponseEntity.status(HttpStatus.OK).body("Task deleted successfully");
	}

}
