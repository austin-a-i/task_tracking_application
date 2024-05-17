package com.springboot.tasktrackingapplication.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
	
	@GetMapping("/view")
	public List<Task> viewTask() {
		
		return null;
	}
	
	@PostMapping("/create")
	public ResponseEntity<TaskResponseDTO> createTask(@RequestBody TaskRequestDTO taskRequest) {
		TaskResponseDTO taskResponse = taskService.saveTask(taskRequest);
		return new ResponseEntity<>(taskResponse,HttpStatus.CREATED);
		
	}
	
	@PutMapping("/update/{name}")
	public Task updateTask() {
		return null;
	}
	
	@DeleteMapping("/delete/{}")
	public String deleteTask() {
		return null;
	}

}
