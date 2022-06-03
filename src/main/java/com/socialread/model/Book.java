package com.socialread.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Book {
	@Id @GeneratedValue
	private int bookid;
	@ManyToOne
	@JoinColumn(name = "user")
	private User owner;
	private String bookname;
	private String author;
	private String publisher;
	private int year;
	private int edition;
	private boolean status;
	@ManyToOne
	private User current_user;
	public User getOwner() {
		return owner;
	}
	public void setOwner(User owner) {
		this.owner = owner;
	}
	
	public User getCurrent_user() {
		return current_user;
	}
	public void setCurrent_user(User current_user) {
		this.current_user = current_user;
	}
	//0 status if taken
	//1 status is available
	public boolean getStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	@Column(name = "date_time", columnDefinition = "TIMESTAMP")
	private LocalDateTime DateTime;
	public LocalDateTime getDateTime() {
		return DateTime;
	}
	public void setDateTime(LocalDateTime dateTime) {
		DateTime = dateTime;
	}
	public int getBookid() {
		return bookid;
	}
	public void setBookid(int bookid) {
		this.bookid = bookid;
	}
	public String getBookname() {
		return bookname;
	}
	public void setBookname(String bookname) {
		this.bookname = bookname;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getPublisher() {
		return publisher;
	}
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public int getEdition() {
		return edition;
	}
	public void setEdition(int edition) {
		this.edition = edition;
	}
	@Override
	public String toString() {
		return "Book [bookid=" + bookid + ", owner=" + owner + ", bookname=" + bookname + ", author=" + author
				+ ", publisher=" + publisher + ", year=" + year + ", edition=" + edition + ", status=" + status
				+ ", current_user=" + current_user + ", DateTime=" + DateTime + "]";
	}
	
}
