package com.socialread;

import java.time.LocalDateTime;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.socialread.dao.UserRepo;
import com.socialread.model.User;

@Controller
public class SignUpController {
	@Autowired
	UserRepo repo;
	@RequestMapping(value = "/signup", method = RequestMethod.GET)
	public ModelAndView getSignUp() {
		ModelAndView mv = new ModelAndView("signup");
		return mv;
	}
	
	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public RedirectView SignUp(User user, RedirectAttributes attributes) {
		RedirectView rv;
		System.out.println(user);
		if(repo.findByemail(user.getEmail()) != null || repo.findByusername(user.getUsername()) != null){
			//either email or username is taken
			rv = new RedirectView("signup");
			attributes.addFlashAttribute("signup_message", "Account already exists with this email! Please login!");
		}
		else {
			//email and username are valid
			
			//verify email using otp
			
			
			rv = new RedirectView("login");
			user.setRegistered_on(LocalDateTime.now());
			repo.save(user);
		}
		
		return rv;
	}

	@RequestMapping(value = "/signup/checkusername", method = RequestMethod.GET)
	@ResponseBody
	public HashMap checkUsername(String username) {
		String result = "false";
		if(repo.findByusername(username) == null) {
			result = "true";
		}
		HashMap<String, String> map = new HashMap<>();
		map.put("result", result);
		return map;
	}
	
}
