package com.expense;

import com.expense.controller.RequestController;
import com.expense.controller.UserController;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;

import javax.servlet.http.HttpSession;

public class EntryPoint {
	public static void main(String...args) {
		Javalin app = Javalin.create().start(8000);

		app.get("/logout", ctx -> {
			HttpSession session = ctx.req.getSession(false);
			if(session != null) session.invalidate();
		});

		app.after(ctx -> {
			ctx.res.addHeader("Access-Control-Allow-Origin", "null");
		});

		app.config.addStaticFiles("/web", Location.CLASSPATH);

		RequestController requestController = new RequestController(app);
		UserController userController = new UserController(app);
	}
}
