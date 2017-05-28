package com.haranku16.controllers;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.haranku16.controllers.util.CookieController;
import com.haranku16.models.User;
import com.haranku16.repositories.UserRepository;

@Controller
public class HomeController {
	@Autowired
	private UserRepository users;
	
	@RequestMapping(path="/", method=RequestMethod.GET)
	public String home(Model model, HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		User user = CookieController.getInstance().getUser(cookies, users);
		if (user == null) {
			return "login";
		}
		model.addAttribute("user", user);
		return "home";
	}
}
