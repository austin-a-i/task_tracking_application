package com.springboot.tasktrackingapplication.entity;

import java.util.List;
import java.util.Set;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class User {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    @Column(name = "username", nullable = false)
	private String username;
	private String email;
	private String password;
	
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name="roles_table",joinColumns = @JoinColumn(name="id"))
    private Set<String>roles;
    
	@ManyToMany
	@JoinTable(name = "User_Task",
			joinColumns = @JoinColumn(name = "User_Id"), 
			inverseJoinColumns = @JoinColumn(name = "Task_ID"))
    private List<Task> tasks;
	
	public User(String username, String email, String password) {
		super();
		this.username = username;
		this.email = email;
		this.password = password;
	}
	
	/*
	{
	        "username":"austin14",
	        "email":"austin14@gmail.com",
	        "password":"1234",
	        "roles":["ADMIN","USER"]
	}
	*/
	
}
