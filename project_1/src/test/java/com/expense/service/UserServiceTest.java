package com.expense.service;

import com.expense.dao.UserDao;
import com.expense.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpSession;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

	@InjectMocks
	private static UserService userService;

	@Mock
	private UserDao userDao;
	@Mock
	private HttpSession session;

	{
		MockitoAnnotations.openMocks(this);
	}

	@BeforeAll
	static void setUp() {
		userService = new UserService();
	}

	@Test
	void testFindByName() {
		String username = "mockedUser";

		Mockito.when(userDao.findByName(username)).thenReturn(
				new User(1, username, "mockedPassword", "admin")
		);

		User user = userService.findByName(username);

		Assertions.assertEquals(1, user.getUserId(), "The fetched user should have an id of 1");
	}

	@Test
	void testLoginSuccess() {
		String username = "mockedUser";
		String password = "mockedPassword";

		Mockito.when(userDao.findByName(username)).thenReturn(
				new User(1, username, password, "admin")
		);

		User user = userService.login(username, password, session);

		Assertions.assertEquals(username, user.getUsername(), "The logged in user should be returned in a successful login");
	}

	@Test
	void testLoginFailure() {
		String username = "mockedUser";
		String password = "mockedPassword";

		Mockito.when(userDao.findByName(username)).thenReturn(
			new User(1, username, "differentPassword", "admin")
		);

		User user = userService.login(username, password, session);

		Assertions.assertNull(user, "A failed login should return null");
	}
}