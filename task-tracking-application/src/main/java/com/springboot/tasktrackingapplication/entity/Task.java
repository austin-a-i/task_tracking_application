package com.springboot.tasktrackingapplication.entity;

import java.time.LocalDate;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Task {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
    
	private String task;
	private String description;
	private LocalDate dueDate;
	@Enumerated(EnumType.STRING)
	private Status status;
	

	@ManyToMany(mappedBy = "tasks")
	@JsonIgnore
    private Set<User> user;
	
}
