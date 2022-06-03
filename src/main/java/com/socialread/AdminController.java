package com.socialread;

import java.util.List;

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

import com.socialread.dao.AdminRepo;
import com.socialread.dao.BookRepo;
import com.socialread.dao.RequestRepo;
import com.socialread.dao.UserRepo;
import com.socialread.model.AdminUser;
import com.socialread.model.Book;
import com.socialread.model.Request;
import com.socialread.model.User;

@Controller
public class AdminController {
	@Autowired
	AdminRepo adminrepo;
	@Autowired
	UserRepo userrepo;
	@Autowired
	BookRepo bookrepo;
	@Autowired
	RequestRepo reqrepo;
	@RequestMapping(value="/adminlogin", method=RequestMethod.GET)
	public String getAdminLogin() {
		return "adminlogin";
	}
	@RequestMapping(value="/adminlogin", method=RequestMethod.POST)
	public RedirectView AdminLogin(@RequestParam("username") String username, @RequestParam("password") String password, HttpSession session, RedirectAttributes attributes) {
		RedirectView rv;
		AdminUser user = adminrepo.findByusername(username);
		if(user != null && user.getPassword().equals(password)) {
			//valid user
			rv = new RedirectView("/admin");
			session.setAttribute("adminuser", username);
			session.setAttribute("isAdminLoggedIn", true);
		}
		else {
			//either password is wrong or user does not exist
			rv = new RedirectView("/adminlogin");
			String message;
			if(user == null)
				message = "No user found with this username";
			else
				message = "Incorrect Password";
			//		session.setAttribute("loginMessage", message);
			attributes.addFlashAttribute("loginMessage", message);
		}
		return rv;
	}
	
	
	
	//admin page controller
	@RequestMapping(value="/admin", method=RequestMethod.GET)
	public ModelAndView adminPage(HttpSession session, RedirectAttributes attributes) {
		ModelAndView mv;
		if(session.getAttribute("isAdminLoggedIn") == null) {
			//not logged in
			mv = new ModelAndView("redirect:/adminlogin");
			attributes.addFlashAttribute("message", "You need to log in to access admin panel!");
		}
		else {
			//user is logged in
			//fetch data from repos and send to model
			
			//fetching total users
			long totalUsers = userrepo.count();
			long totalBooks = bookrepo.count();
			long totalTransactions = reqrepo.countValid().size();
			mv = new ModelAndView("admin");
			mv.addObject("totalUsers", totalUsers);
			mv.addObject("totalBooks", totalBooks);
			mv.addObject("totalTransactions", totalTransactions);
		}
		return mv;
	}
	
	@RequestMapping(value = "/admin/user", method=RequestMethod.GET)
	public ModelAndView allUsers(HttpSession session, RedirectAttributes attributes) {
//		return "this page displays all users info.";
		ModelAndView mv;
		if(session.getAttribute("isAdminLoggedIn") == null) {
			//user is not logged in
			mv = new ModelAndView("redirect:/adminlogin");
			attributes.addFlashAttribute("message", "You need to be logged in to access this page!");
		}
		else {
			Iterable<User> users = userrepo.findAll();
			mv = new ModelAndView("allusers");
			mv.addObject("users", users);
		}
		return mv;		
	}
	@RequestMapping(value = "/admin/book", method=RequestMethod.GET)
	public ModelAndView allBooks(HttpSession session, RedirectAttributes attributes) {
//		return "this page displays all users info.";
		ModelAndView mv;
		if(session.getAttribute("isAdminLoggedIn") == null) {
			//user is not logged in
			mv = new ModelAndView("redirect:/adminlogin");
			attributes.addFlashAttribute("message", "You need to be logged in to access this page!");
		}
		else {
			Iterable<Book> books = bookrepo.findAll();
			mv = new ModelAndView("allbooks");
			mv.addObject("books", books);
		}
		return mv;		
	}
	@RequestMapping(value = "/admin/transaction", method=RequestMethod.GET)
	public ModelAndView allTransactions(HttpSession session, RedirectAttributes attributes) {
//		return "this page displays all users info.";
		ModelAndView mv;
		if(session.getAttribute("isAdminLoggedIn") == null) {
			//user is not logged in
			mv = new ModelAndView("redirect:/adminlogin");
			attributes.addFlashAttribute("message", "You need to be logged in to access this page!");
		}
		else {
			List<Request> transactions = reqrepo.countValid();
			mv = new ModelAndView("alltransactions");
			mv.addObject("transactions", transactions);
		}
		return mv;		
	}
	@RequestMapping(value="/admin/user/{userid}", method=RequestMethod.GET)
	public ModelAndView userInfo(@PathVariable("userid") int userid, HttpSession session, RedirectAttributes attributes) {
		ModelAndView mv;
		if(session.getAttribute("isAdminLoggedIn") == null) {
			mv = new ModelAndView("/adminlogin");
			attributes.addFlashAttribute("message", "You need to be logged in for accessing this page!");
			return mv;
		}
		User user = userrepo.findById(userid).orElse(null);
		List<Book> books = bookrepo.findByOwner(user);
		List<Request> requests = reqrepo.findAllRequests(user);
		List<Request> requests_to_me = reqrepo.findAllRequestsToMe(user);
		mv = new ModelAndView("user");
		mv.addObject("user", user);
		mv.addObject("books", books);
		mv.addObject("requests_from_me", requests);
		mv.addObject("requests_to_me", requests_to_me);
		return mv;
	}
	@RequestMapping(value="/admin/user/{userid}", method=RequestMethod.DELETE)
	public RedirectView deleteUser(@PathVariable("userid")int userid, HttpSession session, RedirectAttributes attributes) {
		RedirectView rv;
		if(session.getAttribute("isAdminLoggedIn") == null) {
			//unauthorised action
			rv = new RedirectView("/adminlogin");
			attributes.addFlashAttribute("message", "Only admins can perform such actions!Login as Admin");
		}
		else {
			User user = userrepo.findById(userid).orElse(null);
			rv = new RedirectView("/admin/user");
			if(user == null) {
				//user not found with that id
				attributes.addFlashAttribute("message", "No such user found!");
			}
			else {
				userrepo.deleteById(userid);
			}
		}
		return rv;
	}
	
	
//	@RequestMapping(value="/admin/book", method=RequestMethod.GET)
//	public ModelAndView getAllBooks(HttpSession session, RedirectAttributes attributes) {
//		ModelAndView mv;
//		if(session.getAttribute("isAdminLoggedIn") == null) {
//			mv = new ModelAndView("/adminlogin");
//			attributes.addFlashAttribute("message", "You need to log in to access this page!");
//		}
//		else {
//			//fetch all books and send to view
//			Iterable<Book> books = bookrepo.findAll();
//		}
//			
//	}
	
	@RequestMapping(value = "/admin/book/{bookid}", method = RequestMethod.GET)
	public ModelAndView viewBook(@PathVariable("bookid") int bookid,HttpSession session, RedirectAttributes attributes) {
		ModelAndView mv;
		if(session.getAttribute("isAdminLoggedIn") == null) {
			mv = new ModelAndView("/adminlogin");
			attributes.addFlashAttribute("message", "You need to be logged in for accessing this page!");
			return mv;
		}
		Book book = bookrepo.findById(bookid).orElse(null);
		mv = new ModelAndView("book-adminview");
		mv.addObject("book", book);
		return mv;
		
	}
	
	@RequestMapping(value="/admin/book/{bookid}", method=RequestMethod.DELETE)
	public RedirectView deleteBook(@PathVariable("bookid")int bookid, HttpSession session, RedirectAttributes attributes) {
		RedirectView rv;
		if(session.getAttribute("isAdminLoggedIn") == null) {
			//unauthorised action
			rv = new RedirectView("/adminlogin");
			attributes.addFlashAttribute("message", "Only admins can perform such actions!Login as Admin");
		}
		else {
			Book book = bookrepo.findById(bookid).orElse(null);
			rv = new RedirectView("/admin");
			if(book == null) {
				//user not found with that id
				attributes.addFlashAttribute("message", "No such user found!");
			}
			else {
				userrepo.deleteById(bookid);
			}
		}
		return rv;
	}
	
}
