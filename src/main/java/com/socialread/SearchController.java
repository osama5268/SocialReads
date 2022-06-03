package com.socialread;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.socialread.dao.BookRepo;
import com.socialread.model.Book;

@Controller
public class SearchController {
	@Autowired
	BookRepo repo;
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public ModelAndView resultPage(@RequestParam("q") String query, HttpSession session, RedirectAttributes attributes) {
		if(session.getAttribute("isLoggedIn") == null || session.getAttribute("isLoggedIn").toString() == "false") {
			attributes.addFlashAttribute("message", "You need to be logged in to search a book!");
			return new ModelAndView("redirect:/login");
		}
		ModelAndView mv = new ModelAndView("search");
		List<Book> resultList = repo.searchByName(query);
		mv.addObject("query", query);
		mv.addObject("resultList", resultList);
		return mv;
	}
}
