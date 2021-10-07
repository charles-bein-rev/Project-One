package com.expense.controller;

import com.expense.model.User;
import com.expense.service.UserService;
import io.javalin.Javalin;
import io.javalin.http.Handler;

import javax.servlet.http.HttpSession;

import static io.javalin.apibuilder.ApiBuilder.path;
import static io.javalin.apibuilder.ApiBuilder.post;

public class UserController {
	private UserService userService;

	public UserController(Javalin app) {
		this.userService = new UserService();
		app.routes(() -> {
			path("login", () -> {
				post(login);
			});
		});
	}

	private Handler login = ctx -> {
		if(ctx.req.getSession(false) == null) {
			System.out.println("No session");
			HttpSession session = ctx.req.getSession();
			String username = ctx.formParam("username");
			String password = ctx.formParam("password");
			User user = this.userService.login(username, password, session);
			if(user != null) {
				ctx.json(user);
				ctx.status(200);
			} else {
				session.invalidate();
			}
		}
	};
}
