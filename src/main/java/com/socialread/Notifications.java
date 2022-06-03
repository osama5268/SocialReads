package com.socialread;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.socialread.dao.RequestRepo;
import com.socialread.dao.UserRepo;
import com.socialread.model.Request;
import com.socialread.model.User;
@Service
public class Notifications {
	@Autowired
	RequestRepo reqrepo;
	@Autowired
	UserRepo userrepo;
	public List<Request> getLatestNotifications(String username){
		if(username == "guest") {
			List<Request> list = new ArrayList<Request>();
			return list;
		}
		User user = userrepo.findByusername(username);
		List<Request> list1 = reqrepo.findBygivenby(user);
		List<Request> list2 = reqrepo.findBytakenby(user);
		List<Request> list = new ArrayList<Request>();
		System.out.println(list2.size());
		list.addAll(list1);
		list.addAll(list2);
		Collections.sort(list);
//		System.out.println(list.size());
		return list;
	}
}
