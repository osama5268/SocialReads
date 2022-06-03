package com.socialread;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.socialread.dao.BookRepo;
import com.socialread.dao.RequestRepo;
import com.socialread.dao.UserRepo;
import com.socialread.model.Book;
import com.socialread.model.Request;
import com.socialread.model.User;

@Controller
public class ProfileController {
	@Autowired
	UserRepo userrepo;
	@Autowired
	BookRepo bookrepo;
	@Autowired
	RequestRepo reqrepo;
	
	@RequestMapping(value = "/profile", method = RequestMethod.GET)
	public ModelAndView profilePage(HttpSession session, RedirectAttributes attributes) {
		ModelAndView mv;
		if(session.getAttribute("isLoggedIn") == null) {
			return new ModelAndView("redirect:/login");
		}
		else if(session.getAttribute("isLoggedIn").toString() == "false") {
			return new ModelAndView("redirect:/login");
		}
		String username = session.getAttribute("username").toString();
		User user = userrepo.findByusername(username);
		List<Request> requests_to_me = reqrepo.findBygivenby(user);
		List<Request> requests_from_me = reqrepo.findAllRequests(user);
		List<String> statusMessage = new ArrayList<String>(); 
		for(Request req: requests_from_me) {
			if(req.isStatus()) {
				statusMessage.add("Approved");
			}
			else {
				if(req.getLast_updated().equals(req.getRequested_on())) {
					statusMessage.add("Waiting for approval");
				}
				else {
					statusMessage.add("Rejected");
				}
			}
		}
		List<Book> books = bookrepo.findByOwner(user);
		List<Request> borrowed_ = reqrepo.findAllByCurrentUser(user);
		List<Request> borrowed = new ArrayList<>();
		for(Request req: borrowed_) {
			if(req.getExchanged_on().plusDays(req.getDays()).isBefore(LocalDateTime.now())) {
				//this user has the book
				int daysLeft =(int) LocalDateTime.now().until(req.getExchanged_on().plusDays(req.getDays()), ChronoUnit.DAYS);
				req.setDaysLeft(daysLeft);
				borrowed.add(req);
			}
		}
		mv = new ModelAndView("profile");
		mv.addObject("requests_to_me", requests_to_me);
		mv.addObject("requests_from_me", requests_from_me);
		mv.addObject("borrowedBooks", borrowed);
//		System.out.println("request from me size:"+ requests_from_me.size());
		mv.addObject("books", books);
		mv.addObject("user", user);
		return mv;		
	}
	@RequestMapping(value = "/profile/edit", method = RequestMethod.GET)
	public ModelAndView editPage(HttpSession session, RedirectAttributes attributes) {
		
		if(session.getAttribute("isLoggedIn") == null) {
			return new ModelAndView("redirect:/login");
		}
		else if(session.getAttribute("isLoggedIn").toString() == "false") {
			return new ModelAndView("redirect:/login");
		}
		ModelAndView mv = new ModelAndView("edit");
		String username = session.getAttribute("username").toString();
		User user = userrepo.findByusername(username);
		mv.addObject("user", user);
		return mv;
	}
	@RequestMapping(value = "/profile/edit", method = RequestMethod.POST)
	public RedirectView edit(User user, HttpSession session, RedirectAttributes attributes) {
		if(session.getAttribute("isLoggedIn") == null) {
			return new RedirectView("/login");
		}
		else if(session.getAttribute("isLoggedIn").toString() == "false") {
			return new RedirectView("/login");
		}
		User user_ = userrepo.findByusername(session.getAttribute("username").toString());
		user_.setName(user.getName());
		user_.setUsername(user.getUsername());
		user_.setAddress(user.getAddress());
		user_.setPhone_number(user.getPhone_number());
		user_.setEmail(user.getEmail());
		userrepo.save(user_);
		session.setAttribute("username", user_.getUsername());
		RedirectView rv = new RedirectView("/profile");
		return rv;
	}
	
}
