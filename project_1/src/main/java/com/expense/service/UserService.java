package com.expense.service;

import com.expense.dao.UserDao;
import com.expense.model.User;

import javax.servlet.http.HttpSession;

public class UserService {

	private UserDao userDao;

	public UserService() {
		this.userDao = new UserDao();
	}

	public User findByName(String username) {
		return this.userDao.findByName(username);
	}

	public User login(String username, String password, HttpSession session) {
		User user = this.findByName(username);
		if(user.getPassword().equals(password)) {
			session.setAttribute("username", user.getUsername());
			session.setAttribute("role", user.getRole());

			return user;
		} else {
			return null;
		}
	}
}
