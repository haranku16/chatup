package com.haranku16.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.haranku16.controllers.util.CookieController;
import com.haranku16.models.User;
import com.haranku16.repositories.UserRepository;

@Controller
public class SearchController {
	@Autowired
	private UserRepository users;
	
	@RequestMapping(path="/search", method=RequestMethod.GET)
	public String search(Model model, HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		User user = CookieController.getInstance().getUser(cookies, users);
		if (user == null) {
			return "login";
		}
		model.addAttribute("user",user);
		return "search";
	}
	
	@RequestMapping(path="/search", method=RequestMethod.POST)
	public String search(Model model, HttpServletRequest request,
			@RequestParam("username") String username, 
			@RequestParam("firstname") String firstname,
			@RequestParam("lastname") String lastname) {
		Cookie[] cookies = request.getCookies();
		User user = CookieController.getInstance().getUser(cookies, users);
		if (user == null) {
			return "login";
		}
		model.addAttribute("user",user);
		List<User> userList = null;
		if (ofMinLength(username,1)) {
			userList = new ArrayList<User>();
			User result = users.findByUsername(username);
			if (result != null) userList.add(result);
			model.addAttribute("result", userList);
			return "search";
		} else {
			if (ofMinLength(firstname,1)) {
				userList = users.findByFirstname(firstname);
			}
			if (ofMinLength(lastname,1)) {
				if (userList == null) userList = users.findByLastname(lastname);
				else userList.containsAll(users.findByLastname(lastname));
			}
			model.addAttribute("result", userList);
			return "search";
		}
	}
	
	private boolean ofMinLength(String str, int length) {
		return str != null && str.length() >= length;
	}
}
