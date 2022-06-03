package com.socialread.dao;
import org.springframework.data.repository.CrudRepository;

import com.socialread.model.AdminUser;

public interface AdminRepo extends CrudRepository<AdminUser,Integer>{
	AdminUser findByusername(String username);
}
