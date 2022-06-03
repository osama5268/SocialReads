package com.socialread.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.socialread.model.Book;
import com.socialread.model.Request;
import com.socialread.model.User;

public interface RequestRepo extends CrudRepository <Request,Integer>{
	
//	@Query(value = "select * from(select * from request where user order by bookid desc limit 10) sub", nativeQuery = true)
//	List<Request> fetchLatestBooks(User user);
	List<Request> findBygivenby(User user);
	@Query(value = "select * from request where taken_by=? and not last_updated = requested_on", nativeQuery = true)
	List<Request> findBytakenby(User user);
	
	@Query(value = "select * from request where taken_by=? and book=?", nativeQuery = true)
	List<Request> checkAlreadyRequested(User user, Book book);
	
	List<Request> findAllBybook(Book book);
	@Query(value = "select * from request where taken_by=?", nativeQuery=true)
	List<Request> findAllRequests(User user);
	
	@Query(value = "select * from request where given_by=? and status=1", nativeQuery=true)
	List<Request> findAllRequestsToMe(User user);
	
	@Query(value = "select * from request where book=? and status=1 order by requestid desc limit 1", nativeQuery = true)
	Request getLatestAcceptedRequest(Book book);
	
	@Query(value = "select * from request where status=1", nativeQuery=true)
	List<Request> countValid();
	
	@Query(value = "select * from request where status=1 and taken_by = ?", nativeQuery = true)
	List<Request> findAllByCurrentUser(User user);
	
}
