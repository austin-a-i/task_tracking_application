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
	
	@Query(value = "SELECT t.* FROM task t JOIN user u ON t.user_id = u.id", nativeQuery = true)		
	List<Task> findallTasks();
	
	@Query(value = "SELECT t.* FROM task t where t.task = :task", nativeQuery = true)
	List<Task> findUsersByTask(@Param("task") String task);
	
	@Query(value = "SELECT t.* FROM task t JOIN user u ON t.user_id = u.id "
											+ "WHERE u.username = :username", nativeQuery = true)
	List<Task> findTasksByUsername(@Param("username") String username);
	
	@Query(value = "SELECT t.* FROM task t JOIN user u ON t.user_id = u.id "
			+ "WHERE u.username = :username and t.task = :task", nativeQuery = true)
	Task findTaskFromUserTasks(@Param("username") String username, @Param("task") String task);
}
