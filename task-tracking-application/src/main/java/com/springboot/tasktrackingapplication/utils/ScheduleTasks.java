package com.springboot.tasktrackingapplication.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.springboot.tasktrackingapplication.services.TaskService;

@Component
public class ScheduleTasks {
	
    @Autowired
    private TaskService taskService;
    
    // Run at midnight every day
    @Scheduled(cron = "0 1 0 * * *")
    public void updateTaskStatus() {
        taskService.updatePastDueTasks();
    }
    
    

}
