package com.springboot.tasktrackingapplication.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.springboot.tasktrackingapplication.dtos.responses.TaskResponseDTO;
import com.springboot.tasktrackingapplication.entity.Task;
import com.springboot.tasktrackingapplication.services.TaskService;


@RestController
@RequestMapping("/tasks")
public class TaskController {
	
	@Autowired
	private TaskService taskService;
	
	@GetMapping("/viewOne/{task}")
	public ResponseEntity<TaskResponseDTO> viewTaskStatus(@PathVariable String task) {
		TaskResponseDTO viewTask = taskService.getTaskStatus(task);
		return new ResponseEntity<>(viewTask, HttpStatus.OK);
	}
	
	@GetMapping("/viewAll")
	public ResponseEntity<List<TaskResponseDTO>> viewAllTask() {
		List<TaskResponseDTO> taskList = taskService.getAllTasks();
		return new ResponseEntity<>(taskList, HttpStatus.OK);
	}
	
	@PostMapping("/create")
	//@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<TaskResponseDTO> createTask(@RequestBody TaskRequestDTO taskRequest) {
		TaskResponseDTO taskResponse = taskService.saveTask(taskRequest);
		return new ResponseEntity<>(taskResponse,HttpStatus.CREATED);
		
	}
	
	@PutMapping("/update/{name}")
	//@PreAuthorize("hasRole('EDITOR') or hasRole('ADMIN')")
	public Task updateTask() {
		return null;
	}
	
	@DeleteMapping("/delete/{}")
	//@PreAuthorize("hasRole('ADMIN')")
	public String deleteTask() {
		return null;
	}

}
