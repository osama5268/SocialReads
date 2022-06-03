package com.socialread.model;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
@Entity
public class Request implements Comparable<Request>{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "requestid", nullable = false)
	private int requestid;
	@ManyToOne
	@JoinColumn(name = "taken_by")
	private User takenby;
	
	public Book getBook() {
		return book;
	}

	
	
	public void setBook(Book book) {
		this.book = book;
	}
	
	private String statusMessage;


	public String getStatusMessage() {
		return statusMessage;
	}



	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}
	@Transient
	private int daysLeft;
	
	public int getDaysLeft() {
		return daysLeft;
	}



	public void setDaysLeft(int daysLeft) {
		this.daysLeft = daysLeft;
	}

	@ManyToOne
	@JoinColumn(name = "given_by")
	private User givenby;
	
	@ManyToOne
	@JoinColumn(name = "book", referencedColumnName = "bookid")
	private Book book;
	private int days;
	@Column(name = "requested_on", columnDefinition = "TIMESTAMP")
	private LocalDateTime requested_on;
	
	private boolean status;
	private String address;
	@Column(name = "exchanged_on", columnDefinition = "TIMESTAMP")
	private LocalDateTime exchanged_on;
	@Column(name ="last_updated", columnDefinition = "TIMESTAMP")
	private LocalDateTime last_updated;
	
	
	
	public int getRequestid() {
		return requestid;
	}



	public void setRequestid(int requestid) {
		this.requestid = requestid;
	}
	
	


	public User getTakenby() {
		return takenby;
	}



	public void setTakenby(User takenby) {
		this.takenby = takenby;
	}



	public User getGivenby() {
		return givenby;
	}



	public void setGivenby(User givenby) {
		this.givenby = givenby;
	}



	public int getDays() {
		return days;
	}



	public void setDays(int days) {
		this.days = days;
	}



	public LocalDateTime getRequested_on() {
		return requested_on;
	}



	public void setRequested_on(LocalDateTime requested_on) {
		this.requested_on = requested_on;
	}



	public boolean isStatus() {
		return status;
	}



	public void setStatus(boolean status) {
		this.status = status;
	}



	public String getAddress() {
		return address;
	}



	public void setAddress(String address) {
		this.address = address;
	}



	public LocalDateTime getExchanged_on() {
		return exchanged_on;
	}



	public void setExchanged_on(LocalDateTime exchanged_on) {
		this.exchanged_on = exchanged_on;
	}



	public LocalDateTime getLast_updated() {
		return last_updated;
	}



	public void setLast_updated(LocalDateTime last_updated) {
		this.last_updated = last_updated;
	}
	
	


	@Override
	public String toString() {
		return "Request [requestid=" + requestid + ", takenby=" + takenby + ", statusMessage=" + statusMessage
				+ ", givenby=" + givenby + ", book=" + book + ", days=" + days + ", requested_on=" + requested_on
				+ ", status=" + status + ", address=" + address + ", exchanged_on=" + exchanged_on + ", last_updated="
				+ last_updated + "]";
	}



	public int compareTo(Request request) {
		if(this.last_updated.isBefore(request.last_updated))
			return -1;
		else
			return 1;
	}
}
