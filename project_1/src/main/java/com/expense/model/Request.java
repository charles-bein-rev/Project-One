package com.expense.model;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;

@Entity

@Table(name = "requests")
public class Request {

	@Id
	@Column
	private int request_id;
	@Column
	private String short_description;
	@Column
	private float amount;
	@Column
	private String long_description;
	@Column
	private String status;


	@ManyToOne
	@JoinColumn(name = "requester_id")
	private User requester;

	private Request() {

	}

	public Request(User user, int requestId, String shortDescription, float amount, String longDescription, String status) {
		setRequester(user);
		setRequestId(requestId);
		setShortDescription(shortDescription);
		setAmount(amount);
		setLongDescription(longDescription);
		setStatus(status);
	}

	public Request(User user, String shortDescription, float amount, String longDescription) {
		setRequester(user);
		setShortDescription(shortDescription);
		setAmount(amount);
		setLongDescription(longDescription);
		setStatus("submitted");
	}

	public User getRequester() {
		return requester;
	}

	public void setRequester(User requester) {
		this.requester = requester;
	}

	public int getRequestId() {
		return request_id;
	}

	public void setRequestId(int requestId) {
		this.request_id = requestId;
	}

	public String getShortDescription() {
		return short_description;
	}

	public void setShortDescription(String shortDescription) {
		this.short_description = shortDescription;
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public String getLongDescription() {
		return long_description;
	}

	public void setLongDescription(String longDescription) {
		this.long_description = longDescription;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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
		return "Request{" +
				"request_id=" + request_id +
				", short_description='" + short_description + '\'' +
				", amount=" + amount +
				", long_description='" + long_description + '\'' +
				", status='" + status + '\'' +
				", requester=" + requester +
				'}';
	}
}
