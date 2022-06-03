package com.socialread;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
public class RequestController {
		@Autowired
		RequestRepo reqrepo;
		@Autowired
		BookRepo bookrepo;
		@Autowired
		UserRepo userrepo;
//		@RequestMapping(value = "/request", method = RequestMethod.GET)
//		public String getRequestPage() {
//			return "request";
//		}
		@RequestMapping(value = "/request/{bookid}", method = RequestMethod.POST)
		public String bookRequested(@PathVariable("bookid") int bookid,@RequestParam("days")int days, HttpSession session, RedirectAttributes attributes) {
			if(session.getAttribute("username") == null || session.getAttribute("username").toString().equals("guest")) {
				//user not logged in
				attributes.addFlashAttribute("message", "You need to be logged in to request a book!");
				return "redirect:/login";
				
			}
			Request request = new Request();
			Book book = bookrepo.findById(bookid).orElse(null);
			if(!book.getStatus()) {
				//book is taken
				return "redirect:/view/"+bookid;
			}
			User requested_by = userrepo.findByusername(session.getAttribute("username").toString());
			User requested_to = book.getOwner();
			request.setGivenby(requested_to);
			request.setTakenby(requested_by);
			request.setDays(days);
			request.setStatus(false);
			request.setStatusMessage("Waiting for Approval");
			LocalDateTime lt = LocalDateTime.now();
			request.setRequested_on(lt);
			request.setLast_updated(lt);
			request.setBook(book);
			reqrepo.save(request);
			return "redirect:/view/"+bookid;
		}
		@RequestMapping(value = "request/accept/{reqid}", method = RequestMethod.GET)
		public ModelAndView getRequest(@PathVariable("reqid") int reqid,RedirectAttributes attributes, HttpSession session) {
			if(session.getAttribute("isLoggedIn") == null ||session.getAttribute("isLoggedIn").toString() == "false") {
				attributes.addFlashAttribute("message", "You need to be logged in to access that feature!");
				return new ModelAndView("redirect:/login");
			}
			Request req = reqrepo.findById(reqid).orElse(null);
			if(req == null) {
				return new ModelAndView("redirect:/");
			}
//			System.out.println(req);
			ModelAndView mv = new ModelAndView("accept");
			mv.addObject("req", req);
			return mv;
		}
		@RequestMapping(value = "request/accept/{reqid}", method = RequestMethod.POST)
		public RedirectView acceptRequest(@PathVariable("reqid")int reqid, @RequestParam("date") String date, @RequestParam("time") String time, @RequestParam("place") String place, HttpSession session, RedirectAttributes attributes) {
			RedirectView rv;
			if(session.getAttribute("isLoggedIn").toString() == "false") {
				rv = new RedirectView("login");
				attributes.addFlashAttribute("message", "You need to be logged in to access that feature!");
				return rv;
			}
			
			User user = userrepo.findByusername(session.getAttribute("username").toString());
			Request req = reqrepo.findById(reqid).orElse(null);
			if(req == null) {
				return new RedirectView("/view/"+req.getBook().getBookid());						
			}
			if(req.getGivenby().getUsername() != user.getUsername()) {
				return new RedirectView("/view/"+req.getBook().getBookid());
				
			}
			//publisher has accepted this request
			//update the request object
			
			req.setAddress(place);
			req.setLast_updated(LocalDateTime.now());
			
			//convert date and time from form to localdatetime
			 System.out.println(date);
			 System.out.println(time);
			 
			 String datetime = date +' '+ time;
			 DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
			 LocalDateTime lt = LocalDateTime.parse(datetime, dtf);
			 req.setExchanged_on(lt);
			 req.setStatus(true);
			 req.setStatusMessage("Accepted");
			 req.getBook().setStatus(false);
			 reqrepo.save(req);
			 
			 User u1 = req.getGivenby();
			 User u2 = req.getTakenby();
			 u1.setBooks_given(u1.getBooks_given()+1);
			 u2.setBooks_taken(u2.getBooks_taken() + 1);
			 userrepo.save(u1);
			 userrepo.save(u2);
			 
			rv = new RedirectView("/view/"+req.getBook().getBookid());
			return rv;
		}
		@RequestMapping(value = "request/reject/{reqid}", method = RequestMethod.POST)
		public RedirectView requestRejected(@PathVariable("reqid") int reqid, RedirectAttributes attributes, HttpSession session) {
			RedirectView rv;
			Request req = reqrepo.findById(reqid).orElse(null);
			if(session.getAttribute("isLoggedIn") == null || session.getAttribute("isLoggedIn").toString() == "false") {
				rv = new RedirectView("login");
				attributes.addFlashAttribute("message", "You need to be logged in to access that feature!");
				return rv;
			}
			String username = session.getAttribute("username").toString();
			if(username == req.getTakenby().getUsername()) {
				//valid
				req.setLast_updated(LocalDateTime.now());
				req.setStatus(false);
				req.setStatusMessage("Rejected");
				reqrepo.save(req);
				rv = new RedirectView("/view/"+req.getBook().getBookid());
			}
			else {
				//invalid
				rv = new RedirectView("/view/"+req.getBook().getBookid());
			}
			return rv;
		}
		
		@RequestMapping(value = "/request/delete/{reqid}", method = RequestMethod.GET)
		public RedirectView deleteRequest(@PathVariable("reqid")int reqid,HttpSession session) {
			if(session.getAttribute("isLoggedIn") == null)
				return new RedirectView("/login");
			else if(session.getAttribute("isLoggedIn").toString() == "false") {
				return new RedirectView("/profile");
			}
			String username = session.getAttribute("username").toString();
			Request req = reqrepo.findById(reqid).orElse(null);
			if(req == null) {
				return new RedirectView("/profile");
			}
			if(req.getTakenby().getUsername().equals(username)) {
				reqrepo.deleteById(reqid);
			}
			return new RedirectView("/profile");
		}
}
