package com.springboot.tasktrackingapplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TaskTrackingApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaskTrackingApplication.class, args);
	}

}
