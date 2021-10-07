package com.expense.model;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;

@Entity

@Table(name = "users")
public class User {
	@Id
	@Column
	private int user_id;
	@Column(unique = true)
	private String username;
	@Column
	private String password;
	@Column
	private String role;

	public User() {

	}

	public User(int userId, String username, String password, String role) {
		setUserId(userId);
		setUsername(username);
		setPassword(password);
		setRole(role);
	}

	public int getUserId() {
		return user_id;
	}

	public void setUserId(int userId) {
		this.user_id = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}

	@Override
	public String toString() {
		return super.toString();
	}
}
