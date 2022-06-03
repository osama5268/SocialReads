package com.socialread.dao;

import org.springframework.data.repository.CrudRepository;

import com.socialread.model.User;

public interface UserRepo extends CrudRepository <User,Integer>{
	User findByusername(String username);
	User findByemail(String email);
}
