package com.socialread;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.socialread.dao.BookRepo;
import com.socialread.dao.RequestRepo;
import com.socialread.dao.UserRepo;
import com.socialread.model.Book;
import com.socialread.model.Request;
import com.socialread.model.User;

@Controller
public class ViewController {
	@Autowired
	BookRepo bookrepo;
	@Autowired
	RequestRepo reqrepo;
	@Autowired
	UserRepo userrepo;
	@RequestMapping(value = "/view/{bookid}", method = RequestMethod.GET)
	public ModelAndView getView(@PathVariable("bookid")int bookid, HttpSession session, RedirectAttributes attributes) {
//		String bookid = request.getPathInfo().substring(1);
		ModelAndView mv;
		if(session.getAttribute("username") == null || session.getAttribute("username").toString().equals("guest")) {
			//user not logged in
			mv = new ModelAndView("redirect:/login");
			attributes.addFlashAttribute("message", "You need to be logged in to access view page!");
			return mv;
		}
		mv = new ModelAndView("view");
//		System.out.println(bookid);
		Book book = bookrepo.findById(bookid).orElse(null);
		boolean isRequested = false;
		User user = userrepo.findByusername(session.getAttribute("username").toString());
		boolean isPublisher = false;
		if(user == book.getOwner()) {
			//this user has published the book
			isPublisher = true;
		}
		List<Request> r= reqrepo.checkAlreadyRequested(user, book);
		for(Request req: r) {
			if(req.isStatus()) {
				if(req.getExchanged_on().plusDays(req.getDays()).isAfter(LocalDateTime.now())) {
					isRequested = true;
					break;
				}
			}
			else {
				isRequested = true;
				break;
			}
		}
		
		
		if(isPublisher) {
			//we will send the requests to view page only if user is publisher
			List<Request> requests = reqrepo.findAllBybook(book);
			Request request;
			mv.addObject("requests", requests);
			request = reqrepo.getLatestAcceptedRequest(book);
			System.out.println(request);
			if(request != null && request.getExchanged_on().isBefore(LocalDateTime.now()) && LocalDateTime.now().isBefore(request.getExchanged_on().plusDays(request.getDays()))){
				//this is the current user
				book.setCurrent_user(request.getTakenby());
				long daysLeft = LocalDateTime.now().until(request.getExchanged_on().plusDays(request.getDays()), ChronoUnit.DAYS);
				System.out.println(daysLeft);
				mv.addObject("daysLeft", daysLeft);
			}
			else {
				book.setCurrent_user(null);
			}
			bookrepo.save(book);
		}
		mv.addObject("book", book);
		mv.addObject("isRequested", isRequested); 
		mv.addObject("isPublisher", isPublisher);
		return mv;
	}
	
	@RequestMapping(value="/view/{bookid}/setStatus/{status}", method=RequestMethod.GET)
	@ResponseBody
	public void setStatus(@PathVariable int bookid, @PathVariable("status") int status, HttpSession session) {
		System.out.println("here1");
		if(session.getAttribute("isLoggedIn") != null) {
			Book book = bookrepo.findById(bookid).orElse(null);
//			System.out.println(session.getAttribute("username").toString());
			if(session.getAttribute("username").toString().equals(book.getOwner().getUsername())) {
				//authorized
				System.out.println("here2");
				boolean status_ = (status == 0)?false:true;
				book.setStatus(status_);
				bookrepo.save(book);
			}
		}
	}
}
