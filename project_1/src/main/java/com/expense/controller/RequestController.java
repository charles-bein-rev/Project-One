package com.expense.controller;

import com.expense.model.Request;
import com.expense.service.RequestService;
import com.expense.service.UserService;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.eclipse.jetty.util.ajax.JSON;

import javax.servlet.http.HttpSession;

import java.io.IOException;

import static io.javalin.apibuilder.ApiBuilder.*;

public class RequestController {

	private RequestService requestService;

	public RequestController(Javalin app) {
		this.requestService = new RequestService();

		app.routes(() -> {
			path("request", () -> {
				path("all", () -> {
					get(findAllRequests);
					path(":id", () -> {
						get(findAllRequestsByUser);
					});
				});
				path("new", () -> {
					post(saveRequest);
				});
				path("aggregate", () -> {
					get(aggregate);
				});
				path(":id", () -> {
					get(findRequestById);
					path("update/:status", () -> {
						post(approveOrDeny);
					});
				});
			});
		});
	}

	private void noSession(Context ctx) throws IOException {
		ctx.res.getWriter().write("You do not have a session");
//		ctx.redirect("/pages/login.html");
	}

	private Handler findRequestById = ctx -> {
		HttpSession session = ctx.req.getSession(false);

		if(session != null)
			ctx.json(this.requestService.findById(Integer.parseInt(ctx.pathParam("id"))));
		else
			noSession(ctx);
	};

	private Handler findAllRequests = ctx -> {

		HttpSession session = ctx.req.getSession(false);

		if(session != null)
			ctx.json(this.requestService.findAll());
		else
			noSession(ctx);
	};

	private Handler findAllRequestsByUser = ctx -> {
		HttpSession session = ctx.req.getSession(false);

		if(session != null) {
			ctx.json(this.requestService.findByUser(Integer.parseInt(ctx.pathParam("id"))));
		} else {
			noSession(ctx);
		}
	};

	private Handler saveRequest = ctx -> {
		HttpSession session = ctx.req.getSession(false);
		if(session != null) {
			Request request = new Request(
					new UserService().findByName((String) session.getAttribute("username")),
					ctx.formParam("short_description"),
					Float.parseFloat(ctx.formParam("amount")),
					ctx.formParam("long_description")
			);

			this.requestService.save(request);
			ctx.status(201);
		} else {
			noSession(ctx);
		}
	};

	private Handler approveOrDeny = ctx -> {
		HttpSession session = ctx.req.getSession(false);
		if(session != null && session.getAttribute("role").equals("admin")) {
			this.requestService.approveOrDeny(Integer.parseInt(ctx.pathParam("id")), ctx.pathParam("status"));
		} else {
			noSession(ctx);
		}
	};

	private Handler aggregate = ctx -> {
		HttpSession session = ctx.req.getSession(false);
		if(session != null) {
			ctx.contentType("json");
			ctx.result(this.requestService.aggregation());
		}
	};
}
