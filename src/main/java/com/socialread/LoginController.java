package com.socialread;

import java.io.IOException;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.socialread.dao.RequestRepo;
import com.socialread.dao.UserRepo;
import com.socialread.model.Request;
import com.socialread.model.User;

@Controller
public class LoginController {
	@Autowired
	Notifications notifications;
	@Autowired
	UserRepo repo;
	@Autowired 
	RequestRepo reqrepo;
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String LoginPage(Model model) {
		return "login";
	}
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public RedirectView Login(@RequestParam("username") String username,@RequestParam("password") String password, RedirectAttributes attributes, HttpSession session){
		User user = repo.findByusername(username);
		//	System.out.println(user);
		if(user != null && user.getPassword().equals(password)) {
			//valid user
			RedirectView rv = new RedirectView("/");
			session.setAttribute("username", username);
			List<Request> requests = notifications.getLatestNotifications(session.getAttribute("username").toString());
			session.setAttribute("isLoggedIn", true);
			session.setAttribute("notifications", requests);
			return rv;
		}
		else {
			//either password is wrong or user does not exist
			RedirectView rv = new RedirectView("login");
			String message;
			if(user == null)
				message = "No user found with this username";
			else
				message = "Incorrect Password";
			//		session.setAttribute("loginMessage", message);
			attributes.addFlashAttribute("loginMessage", message);
			return rv;
		}
	}
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public RedirectView Logout(HttpSession session) {
		RedirectView rv = new RedirectView("/");
		session.removeAttribute("username");
		session.removeAttribute("isLoggedIn");
		return rv;
	}
	
	
	@RequestMapping(value = "/forgotpwd", method = RequestMethod.GET)
	public String getForgotPassword() {
		return "forgotpwd";
	}
	@RequestMapping(value = "/validate", method = RequestMethod.GET)
	public String getValidate() {
		return "validate";
	}
	@RequestMapping(value = "/changepwd", method = RequestMethod.GET)
	public String getChangePassword() {
		return "changepwd";
	}
	private int otp;
	private String email_;
	@RequestMapping(value = "/forgotpwd", method = RequestMethod.POST)
	public RedirectView sendOtp(@RequestParam("email") String email, RedirectAttributes attributes) throws AddressException, MessagingException, IOException {
		RedirectView rv;
		//check if account with such email exists
		User user = repo.findByemail(email);
		if(user == null) {
			//no account with that email exists
			
			rv = new RedirectView("forgotpwd");
			attributes.addFlashAttribute("message", "We could'nt find any account with this email!");
			return rv;
		}
		SendEmail e = new SendEmail();
		email_ = email;
		otp = e.sendmail(email);
		rv = new RedirectView("validate");
		return rv;
	}
	@RequestMapping(value = "/validate", method = RequestMethod.POST)
	public RedirectView validateOtp(@RequestParam("otp")int user_otp, RedirectAttributes attributes) {
		RedirectView rv;
		if(user_otp == otp) {
			//return change password screen
			rv = new RedirectView("changepwd");
		}
		else {
			//otp did not match
			attributes.addFlashAttribute("message", "OTP did not match!");
			rv = new RedirectView("validate");
		}
		return rv;
	}
	@RequestMapping(value = "/changepwd", method = RequestMethod.POST)
	public RedirectView changePassword(@RequestParam("password") String password){
		User user = repo.findByemail(email_);
		user.setPassword(password);
		RedirectView rv = new RedirectView("login");
		return rv;
	}
	
}
	

