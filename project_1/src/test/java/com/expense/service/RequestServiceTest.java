package com.expense.service;

import com.expense.dao.RequestDao;
import com.expense.exception.InvalidOptionException;
import com.expense.exception.NoResultsException;
import com.expense.model.Request;
import com.expense.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RequestServiceTest {

	@InjectMocks
	private static RequestService requestService;

	@Mock
	private RequestDao requestDao;

	{
		MockitoAnnotations.openMocks(this);
	}

	@BeforeAll
	static void setUp() {
		requestService = new RequestService();
	}

	@Test
	void testFindByIdRequestExists() throws NoResultsException {
		int id = 1;

		Mockito.when(requestDao.findById(id)).thenReturn(
				new Request(new User(), 1, "mockedShortDescription", 500.00f, "mockedLongDescription", "submitted")
		);


		Request request = requestService.findById(id);

		Assertions.assertEquals(id, request.getRequestId(), "The fetched request should have the id used");
	}

	@Test
	void testFindByIdRequestDoesNotExist() {
		int id = 2;

		Mockito.when(requestDao.findById(id)).thenReturn(null);

		Assertions.assertThrows(NoResultsException.class, () -> requestService.findById(id), "If no results are found, a NoResultsException should be thrown");
	}

	@Test
	void testFindAllSuccess() throws NoResultsException {
		Mockito.when(requestDao.findAll()).thenReturn(
				Arrays.asList(
						new Request(new User(), 1, "mockedShortDescription", 500.00f, "mockedLongDescription", "submitted"),
						new Request(new User(), 2, "secondMockedShortDescription", 135.34f, "secondMockedLongDescription", "rejected")
			)
		);

		List<Request> requests = requestService.findAll();

		Assertions.assertEquals(2, requests.size(), "Two requests should have been returned");
	}

	@Test
	void testFindAllNoResults() {
		Mockito.when(requestDao.findAll()).thenReturn(Arrays.asList());

		Assertions.assertThrows(NoResultsException.class, () -> requestService.findAll());
	}

	@Test
	void testFindByUserSuccess() throws NoResultsException {
		int id = 1;

		Mockito.when(requestDao.findAll()).thenReturn(
				Arrays.asList(
						new Request(new User(1, "user1", "pass1", "admin"), 1, "shortDescription", 500.00f, "longDescription", "submitted"),
						new Request(new User(1, "user1", "pass1", "admin"), 2, "shortDescription", 235.00f, "longDescription", "accepted"),
						new Request(new User(2, "user2", "pass2", "associate"), 3, "shortDescription", 123.45f, "longDescription", "rejected")
				)
		);

		List<Request> requests = requestService.findByUser(id);

		Assertions.assertEquals(2, requests.size(), "Only the requests associated with the passed user should be returned");
	}

	@Test
	void testFindByUserNoResults() {
		int id = 2;

		Mockito.when(requestDao.findAll()).thenReturn(
				Arrays.asList(
						new Request(new User(1, "user1", "pass1", "admin"), 1, "shortDescription", 500.00f, "longDescription", "submitted"),
						new Request(new User(1, "user1", "pass1", "admin"), 2, "shortDescription", 235.00f, "longDescription", "accepted")
				)
		);

		Assertions.assertThrows(NoResultsException.class, () -> requestService.findByUser(id), "If no requests are associated with the passed user, a NoResultsException should be thrown");
	}

	@Test
	void testAggregation() throws NoResultsException, JsonProcessingException {
		float value1 = 74.00f;
		float value2 = 20.00f;
		float value3 = 5.00f;
		float value4 = 99.00f;
		float value5 = 0.10f;
		float value6 = 0.20f;
		float value7 = 0.30f;

		User user1 = new User(1, "user1", "pass1", "admin");
		User user2 = new User(2, "user2", "pass2", "associate");
		Mockito.when(requestDao.findAll()).thenReturn(
				Arrays.asList(
						new Request(user1, 1, "shortDesc", value1, "longDesc", "accepted"),
						new Request(user1, 2, "shortDesc", value2, "longDesc", "accepted"),
						new Request(user1, 3, "shortDesc", value3, "longDesc", "rejected"),
						new Request(user2, 4, "shortDesc", value4, "longDesc", "accepted"),
						new Request(user2, 5, "shortDesc", value5, "longDesc", "accepted"),
						new Request(user2, 6, "shortDesc", value6, "longDesc", "submitted"),
						new Request(user2, 7, "shortDesc", value7, "longDesc", "rejected")
				)
		);

		String resultString = requestService.aggregation();

		Map<String, String> resultMap = new ObjectMapper().readValue(resultString, HashMap.class);

		float total = value1 + value2 + value4 + value5;
		float userTotal = value4 + value5 + value6 + value7;
		float userAverage = (value1 + value2 + value3) / 3;

		Assertions.assertEquals(4, Integer.parseInt(resultMap.get("totalCount")), "Three approved requests were found");
		Assertions.assertEquals(total, Float.parseFloat(resultMap.get("totalValue")), "The total should match the total of the amounts of accepted requests");
		Assertions.assertEquals(userTotal, Float.parseFloat(resultMap.get("maxAmount")), "User2 has the higher total value of requests");
		Assertions.assertEquals(userAverage, Float.parseFloat(resultMap.get("averageAmount")), "User1 has the higher average of requests");

	}

	@Test
	void testSaveSuccess() throws InvalidOptionException {
		User user = new User(1, "user1", "pass1", "admin");
		Request request = new Request(user, 1, "shortDesc", 100.00f, "longDesc", "submitted");

		Mockito.doNothing().when(requestDao).save(request);

		requestService.save(request);


		verify(requestDao, description("The save method should have been called")).save(request);

	}

	@Test
	void testSaveInvalidOption() {
		User user = new User(1, "user1", "pass1", "admin");
		Request request = new Request(user, 1, "shortDesc", 100.00f, "longDesc", "accepted");

		Assertions.assertThrows(InvalidOptionException.class, () -> requestService.save(request), "If the status of a new request is not \"submitted\", an InvalidOptionException should be thrown");
	}

	@Test
	void testUpdateSuccess() throws InvalidOptionException {
		User user = new User(1, "user1", "pass1", "admin");
		Request request = new Request(user, 1, "shortDesc", 100.00f, "longDesc", "accepted");

		Mockito.doNothing().when(requestDao).update(request);

		requestService.update(request);

		verify(requestDao, description("The update method should have been called")).update(request);
	}

	@Test
	void testUpdateInvalidOption() {
		User user = new User(1, "user1", "pass1", "admin");
		Request request = new Request(user, 1, "shortDesc", 100.00f, "longDesc", "wrongStatus");

		Assertions.assertThrows(InvalidOptionException.class, () -> requestService.update(request), "If the status of an updated request is invalid, an InvalidOptionException should be thrown");
	}

	@Test
	void testApproveOrDenySuccess() throws NoResultsException, InvalidOptionException {
		int id = 1;
		String status = "rejected";
		User user = new User(1, "user1", "pass1", "admin");
		Request request = new Request(user, id, "shortDesc", 100.00f, "longDesc", "accepted");

		Mockito.when(requestDao.findById(id)).thenReturn(request);
		Mockito.doNothing().when(requestDao).update(request);

		requestService.approveOrDeny(id, status);

		verify(requestDao, description("The request should have been updated")).update(request);
	}

	@Test
	void testDelete() {
		User user = new User(1, "user1", "pass1", "admin");
		Request request = new Request(user, 1, "shortDesc", 100.00f, "longDesc", "accepted");

		Mockito.doNothing().when(requestDao).delete(request);

		requestService.delete(request);

		verify(requestDao, description("The delete method should have been called")).delete(request);
	}
}