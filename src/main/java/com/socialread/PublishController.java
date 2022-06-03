package com.socialread;

import java.time.LocalDateTime;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.socialread.dao.BookRepo;
import com.socialread.dao.UserRepo;
import com.socialread.model.Book;
import com.socialread.model.User;

@Controller
public class PublishController {
	@Autowired
	UserRepo userrepo;
	@Autowired
	BookRepo bookrepo;
	@RequestMapping(value = "/publish", method = RequestMethod.GET)
	public String publish_page(HttpSession session, RedirectAttributes attributes) {
		
		if(session.getAttribute("username") == null || session.getAttribute("username").toString().equals("guest")){
			// not logged in
			attributes.addFlashAttribute("message", "You need to login to access publish feature!");
			return "redirect:/login";
		}
		return "publish";
	}
	
	@RequestMapping(value = "/publish", method = RequestMethod.POST)
	public RedirectView publish(Book book, HttpSession session, RedirectAttributes attributes) {
		RedirectView rv;
		System.out.println("publish request received");
		System.out.println(book);
		String username = session.getAttribute("username").toString();
		User user = userrepo.findByusername(username);
		book.setOwner(user);
		LocalDateTime lt = LocalDateTime.now();
		book.setDateTime(lt);
		book.setStatus(true);
		user.setBooks_published(user.getBooks_published() + 1);
		userrepo.save(user);
		bookrepo.save(book);
		rv = new RedirectView("/");
		return rv;
	}
	@RequestMapping(value = "/publish/{bookid}", method = RequestMethod.DELETE)
	public RedirectView publish(@PathVariable("bookid") int bookid,HttpSession session) {
		//check whether the user requesting the delete is the same as who published it
		Book book = bookrepo.findById(bookid).orElse(null);
		RedirectView rv;
		if(session.getAttribute("username").toString().equals(book.getOwner().getUsername())) {
			//proceed with delete
			rv = new RedirectView("/");
			bookrepo.deleteById(bookid);
			return rv;
		}
		else {
			rv = new RedirectView("/view?bookid="+bookid);
			return rv;
		}
	}
}
