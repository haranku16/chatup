package com.haranku16.controllers;

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
public class ChatController {
	@Autowired
	private UserRepository users;
	
	@RequestMapping(path="/chat", method=RequestMethod.GET)
	public String chat(Model model,
			@RequestParam("username") String username,
			HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		User user = CookieController.getInstance().getUser(cookies, users);
		if (user == null) return "login";
		model.addAttribute("user", user);
		if (username == null) return "home";
		User friend = users.findByUsername(username);
		if (friend == null) return "home";
		if (!user.getChats().keySet().contains(friend.getUsername())) return "home";
		model.addAttribute("friend", friend);
		return "chat";
	}
}
