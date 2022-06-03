package com.socialread.model;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
@Entity
public class User {
	@Id @GeneratedValue
	private int userid;
	private String username;
	private String name;
	private long phone_number;
	private String address;
	private String email;
	private String password;
	private LocalDateTime registered_on;
	private int books_published = 0;
	private int books_given = 0;
	private int books_taken = 0;
	
	public int getBooks_published() {
		return books_published;
	}
	public void setBooks_published(int books_published) {
		this.books_published = books_published;
	}
	public int getBooks_given() {
		return books_given;
	}
	public void setBooks_given(int books_given) {
		this.books_given = books_given;
	}
	public int getBooks_taken() {
		return books_taken;
	}
	public void setBooks_taken(int book_taken) {
		this.books_taken = book_taken;
	}
	public LocalDateTime getRegistered_on() {
		return registered_on;
	}
	public void setRegistered_on(LocalDateTime registered_on) {
		this.registered_on = registered_on;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getPhone_number() {
		return phone_number;
	}
	public void setPhone_number(long phone_number) {
		this.phone_number = phone_number;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	@Override
	public String toString() {
		return "User [userid=" + userid + ", username=" + username + ", name=" + name + ", phone_number=" + phone_number
				+ ", address=" + address + ", email=" + email + ", password=" + password + ", registered_on="
				+ registered_on + ", books_published=" + books_published + ", books_given=" + books_given
				+ ", books_taken=" + books_taken + "]";
	}
	
	
}
