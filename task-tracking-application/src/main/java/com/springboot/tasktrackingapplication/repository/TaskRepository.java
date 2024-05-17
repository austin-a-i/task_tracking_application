package com.springboot.tasktrackingapplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.springboot.tasktrackingapplication.entity.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

}
