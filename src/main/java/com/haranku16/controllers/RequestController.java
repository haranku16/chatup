package com.haranku16.controllers;

import java.io.IOException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.haranku16.controllers.util.CookieController;
import com.haranku16.models.Chat;
import com.haranku16.models.User;
import com.haranku16.repositories.ChatRepository;
import com.haranku16.repositories.UserRepository;

@Controller
public class RequestController {
	@Autowired
	private UserRepository users;
	@Autowired
	private ChatRepository chats;
	
	@RequestMapping(path="/request", method=RequestMethod.GET)
	public String request(Model model, 
			@RequestParam("username") String username, 
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		Cookie[] cookies = request.getCookies();
		User user = CookieController.getInstance().getUser(cookies, users);
		if (user == null) {
			return "login";
		}
		User requested = users.findByUsername(username);
		if (requested != null) {
			requested.getRequests().add(user.getUsername());
		}
		response.sendRedirect("/");
		users.save(requested);
		model.addAttribute("user", user);
		return "home";
	}
	
	@RequestMapping(path="/accept", method=RequestMethod.GET)
	public String accept(Model model,
			@RequestParam("username") String username,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		Cookie[] cookies = request.getCookies();
		User user = CookieController.getInstance().getUser(cookies, users);
		if (user == null) return "login";
		User requester = users.findByUsername(username);
		if (requester != null) {
			Chat chat = new Chat();
			chat.getUsers().add(user.getUsername());
			chat.getUsers().add(requester.getUsername());
			chats.save(chat);
			requester.getChats().put(user.getUsername(), chat.getId());
			users.save(requester);
			user.getChats().put(requester.getUsername(), chat.getId());
			chats.save(chat);
		}
		user.getRequests().remove(requester.getUsername());
		response.sendRedirect("/");
		users.save(user);
		model.addAttribute("user", user);
		return "home";
	}
	
	@RequestMapping(path="/reject", method=RequestMethod.GET)
	public String reject(Model model,
			@RequestParam("username") String username,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		Cookie[] cookies = request.getCookies();
		User user = CookieController.getInstance().getUser(cookies, users);
		if (user == null) return "login";
		User requester = users.findByUsername(username);
		user.getRequests().remove(requester.getUsername());
		users.save(user);
		response.sendRedirect("/");
		model.addAttribute("user", user);
		return "home";
	}
	
	@RequestMapping(path="/unfriend",method=RequestMethod.GET)
	public String unfriend(Model model,
			@RequestParam("username") String username,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		Cookie[] cookies = request.getCookies();
		User user = CookieController.getInstance().getUser(cookies, users);
		if (user == null) return "login";
		User unfriended = users.findByUsername(username);
		if (unfriended != null) {
			user.getChats().remove(unfriended.getUsername());
			users.save(user);
			unfriended.getChats().remove(user.getUsername());
			users.save(unfriended);
		}
		response.sendRedirect("/");
		model.addAttribute("user",user);
		return "home";
	}
}