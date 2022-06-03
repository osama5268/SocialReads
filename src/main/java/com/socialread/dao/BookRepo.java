package com.socialread.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.socialread.model.Book;
import com.socialread.model.User;

public interface BookRepo extends CrudRepository <Book,Integer>{
	Book findBybookname(String username);
//	Book findByuser(User user);
	
	@Query(value = "select * from(select * from book where status=1 order by bookid desc limit 10) sub", nativeQuery = true)
	List<Book> fetchLatestBooks();
	
	@Query(value = "select * from book where status=1 and match(bookname) against(? in natural language mode)", nativeQuery = true)
	List<Book> searchByName(String query);
	
	List<Book> findByOwner(User owner);
}
