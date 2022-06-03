package com.socialread;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.socialread.dao.BookRepo;
import com.socialread.dao.RequestRepo;
import com.socialread.dao.UserRepo;
import com.socialread.model.Book;
import com.socialread.model.Request;
import com.socialread.model.User;

@Controller
public class IndexController {
	@Autowired
	BookRepo bookrepo;
	@Autowired
	RequestRepo reqrepo;
	@Autowired 
	UserRepo userrepo;
	@Autowired
	Notifications notifications;
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView getIndex(HttpSession session) {
		ModelAndView mv = new ModelAndView("index.html");
		
		if(session.getAttribute("username") == null) {
					session.setAttribute("username", "guest");
					session.setAttribute("isLoggedIn", false);
		}
		//fetching latest books from the repo
		List<Book> books = bookrepo.fetchLatestBooks();
//		List<Request> requests = notifications.getLatestNotifications(session.getAttribute("username").toString());
		mv.addObject("books",books);
//		mv.addObject("notifications", requests);
		return mv;
	}
}
