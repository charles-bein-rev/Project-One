package com.expense.service;

import com.expense.dao.RequestDao;
import com.expense.exception.InvalidOptionException;
import com.expense.exception.NoResultsException;
import com.expense.model.Request;
import com.expense.model.User;

import java.util.*;

public class RequestService {

	private RequestDao requestDao;

	private final List<String> statusOptions = Arrays.asList("submitted", "accepted", "rejected");

	public RequestService() {
		this.requestDao = new RequestDao();
	}

	public Request findById(int id) throws NoResultsException {
		Request result = this.requestDao.findById(id);
		if(result != null) {
			return result;
		} else {
			throw new NoResultsException("No request found with supplied id");
		}
	}


	public List<Request> findAll() throws NoResultsException {
		List<Request> requests = this.requestDao.findAll();
		if (!requests.isEmpty())
			return requests;
		else
			throw new NoResultsException("No requests found");
	}

	public List<Request> findByUser(int id) throws NoResultsException {
		List<Request> requests = this.findAll();
		List<Request> result = new ArrayList<>();
		for(Request r : requests) {
			if(r.getRequester().getUserId() == id) {
				result.add(r);
			}
		}
		if(!result.isEmpty())
			return result;
		else
			throw new NoResultsException("No requests were found for the passed user");
	}

	public String aggregation() throws NoResultsException {
		StringBuilder output = new StringBuilder();
		List<Request> requests = this.findAll();
		Map<String, String> pairs = new HashMap<>();
		pairs.putAll(getTotal(requests));
		pairs.putAll(getMaxTotal(requests));
		pairs.putAll(getAvgTotal(requests));

		int runs = 0;
		output.append("{");
		for(String key : pairs.keySet()) {
			output.append("\"").append(key).append("\":\"").append(pairs.get(key)).append("\"");
			if(runs < pairs.size() - 1) {
				output.append(",");
				runs++;
			}
		}
		output.append("}");

		return output.toString();
	}

	private Map<String, String> getTotal(List<Request> requests) {
		int count = 0;
		float value = 0f;

		for(Request request : requests) {
			if(request.getStatus().equals("accepted")) {
				count++;
				value += request.getAmount();
			}
		}

		Map<String, String> results = new HashMap<>();
		results.put("totalCount", String.valueOf(count));
		results.put("totalValue", String.valueOf(value));

		return results;
	}

	private Map<String, String> getMaxTotal(List<Request> requests) {
		Set<User> users = new HashSet<>();
		for(Request request : requests) {
			users.add(request.getRequester());
		}

		User user = null;
		float value = 0;

		for(User u : users) {
			float total = 0f;
			for(Request request : requests) {
				if(request.getRequester().equals(u)) {
					total += request.getAmount();
				}
			}
			if(total > value) {
				value = total;
				user = u;
			}
		}


		Map<String, String> results = new HashMap<>();
		results.put("maxUser", user.getUsername());
		results.put("maxAmount", String.valueOf(value));
		return results;
	}

	private Map<String, String> getAvgTotal(List<Request> requests) {
		Set<User> users = new HashSet<>();
		for(Request request : requests) {
			users.add(request.getRequester());
		}

		User user = null;
		float value = 0;

		for(User u : users) {
			float total = 0f;
			float average = 0f;
			int count = 0;
			for(Request request : requests) {
				if(request.getRequester().equals(u)) {
					total += request.getAmount();
					count++;
				}
			}
			average = total / count;
			if(average > value) {
				value = average;
				user = u;
			}
		}


		Map<String, String> results = new HashMap<>();
		results.put("averageUser", user.getUsername());
		results.put("averageAmount", String.valueOf(value));
		return results;
	}

	public void save(Request request) throws InvalidOptionException {
		if (request.getStatus().equals("submitted")) {
			this.requestDao.save(request);
		} else {
			throw new InvalidOptionException("The request contains an invalid status");
		}
	}

	public void update(Request request) throws InvalidOptionException {
		if (statusOptions.contains(request.getStatus())) {
			this.requestDao.update(request);
		} else {
			throw new InvalidOptionException("The updated request contains an invalid status");
		}
	}

	public void approveOrDeny(int id, String status) throws InvalidOptionException, NoResultsException {
		Request request = this.findById(id);
		request.setStatus(status);
		this.update(request);
	}

	public void delete(Request request) {
		this.requestDao.delete(request);
	}
}
