package com.springboot.tasktrackingapplication.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.springboot.tasktrackingapplication.entity.Task;
import com.springboot.tasktrackingapplication.entity.User;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
	
	List<Task> findByUser(User user);
	
	@Query(value = "SELECT ta FROM Task ta JOIN ta.user u WHERE ta.task = ':task' AND u.username = ':username'")
	Task findByTaskAndUser(@Param("task") String task, @Param("username") String username);
}
