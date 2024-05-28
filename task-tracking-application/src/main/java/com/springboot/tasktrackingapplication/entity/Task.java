package com.springboot.tasktrackingapplication.entity;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "task", uniqueConstraints = {
	    @UniqueConstraint(columnNames = {"task", "user_id"})
	})
public class Task {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
    
	private String task;
	private String description;
	private LocalDate dueDate;
	@Enumerated(EnumType.STRING)
	private Status status;
	
	@ToString.Exclude
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id")
	@JsonIgnoreProperties({"password", "enabled", "authorities"})
    private User user;

	/*
	 * @Override public String toString() { return "Task [id=" + id + ", task=" +
	 * task + ", dueDate=" + dueDate + ", status=" + status + "]"; }
	 */
	
	
}
