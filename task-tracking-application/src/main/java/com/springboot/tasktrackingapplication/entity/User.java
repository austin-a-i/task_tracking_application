package com.springboot.tasktrackingapplication.entity;

import java.util.List;
import java.util.Set;

import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
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
public class User implements UserDetails {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    @Column(name = "username", unique = true, nullable = false)
	private String username;
    @Column(name = "email", nullable = false)
	private String email;
    @Column(name = "password", nullable = false)
	private String password;
    @Column(name = "enabled", nullable = false)
    private boolean enabled;
	
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "user_authority",
            joinColumns = @JoinColumn(name = "User_Id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "Authority_Id", referencedColumnName = "id"))
    private List<Authority> authorities;

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "User_Task",
			joinColumns = @JoinColumn(name = "User_Id"), 
			inverseJoinColumns = @JoinColumn(name = "Task_ID"))
	@JsonIgnore
    private Set<Task> tasks;
	
	public User(String username, String email, String password) {
		super();
		this.username = username;
		this.email = email;
		this.password = password;
	}
	
	public User(String username, String email, String password, boolean enabled, 
																	List<Authority> authorities) {
		super();
		this.username = username;
		this.email = email;
		this.password = password;
		this.enabled = enabled;
		this.authorities = authorities;
	}

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
	
	/*
	{
	        "username":"austin14",
	        "email":"austin14@gmail.com",
	        "password":"1234",
	        "enabled":true
	        "authorities":["ADMIN","USER"]
	}
	*/
	
}
