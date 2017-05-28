package com.haranku16.controllers;

import java.security.SecureRandom;

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
import com.haranku16.models.User;
import com.haranku16.repositories.UserRepository;

@Controller
public class UserController {
	@Autowired
	UserRepository users;
	
	@RequestMapping(path="/login", method=RequestMethod.GET) 
	public String login(Model model, HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		User user = CookieController.getInstance().getUser(cookies, users);
		if (user == null) return "login";
		model.addAttribute("user", user);
		return "home";
	}
	
	@RequestMapping(path="/login", method=RequestMethod.POST)
	public String login(Model model, 
			@RequestParam("username") String username,
			@RequestParam("password") String password,
			HttpServletRequest request, HttpServletResponse response) {
		Cookie[] cookies = request.getCookies();
		User user = CookieController.getInstance().getUser(cookies, users);
		if (user != null) {
			model.addAttribute("user",user);
			return "home";
		}
		user = users.findByUsername(username);
		if (user != null && user.getPassword().equals(password)) {
			model.addAttribute("user", user);
			Cookie cookie1 = new Cookie("username", username);
			response.addCookie(cookie1);
			byte[] bytes = new byte[16];
			SecureRandom random = new SecureRandom();
			random.nextBytes(bytes);
			Cookie cookie2 = new Cookie("session", bytes.toString());
			response.addCookie(cookie2);
			user.getSessions().add(bytes.toString());
			users.save(user);
			return "home";
		}
		model.addAttribute("errorMessage", "Invalid credentials.");
		return "login";
	}
	
	@RequestMapping(path="/logout", method=RequestMethod.GET)
	public String logout(Model model, HttpServletResponse response) {
		Cookie cookie1 = new Cookie("username",null);
		Cookie cookie2 = new Cookie("session", null);
		cookie1.setMaxAge(0);
		cookie2.setMaxAge(0);
		response.addCookie(cookie1);
		response.addCookie(cookie2);
		return "login";
	}
	
	@RequestMapping(path="/register", method=RequestMethod.GET) 
	public String register(Model model, HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		User user = CookieController.getInstance().getUser(cookies, users);
		if (user == null) return "register";
		model.addAttribute("user", user);
		return "home";
	}
	
	@RequestMapping(path="/register", method=RequestMethod.POST) 
	public String register(Model model, 
			@RequestParam("username") String username, 
			@RequestParam("password") String password,
			@RequestParam("password_retype") String retype,
			@RequestParam("firstname") String firstname,
			@RequestParam("lastname") String lastname, HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		User user = CookieController.getInstance().getUser(cookies, users);
		if (user != null) {
			model.addAttribute("user", user);
			return "home";
		}
		if (!ofMinLength(username,1)) {
			model.addAttribute("errorMessage", "You haven't entered a username.");
			model.addAttribute("username",username);
			model.addAttribute("firstname",firstname);
			model.addAttribute("lastname",lastname);
			return "register";
		}
		if (!ofMinLength(password,8)) {
			model.addAttribute("errorMessage", "The password must be at least 8 characters long.");
			model.addAttribute("username",username);
			model.addAttribute("firstname",firstname);
			model.addAttribute("lastname",lastname);
			return "register";
		}
		if (!ofMinLength(firstname,1)) {
			model.addAttribute("errorMessage", "You haven't entered a first name.");
			model.addAttribute("username",username);
			model.addAttribute("firstname",firstname);
			model.addAttribute("lastname",lastname);
			return "register";
		}
		if (!ofMinLength(lastname,1)) {
			model.addAttribute("errorMessage", "You haven't entered a last name.");
			model.addAttribute("username",username);
			model.addAttribute("firstname",firstname);
			model.addAttribute("lastname",lastname);
			return "register";
		}
		if (users.findByUsername(username) != null) {
			model.addAttribute("errorMessage", "This username has been taken.");
			model.addAttribute("username",username);
			model.addAttribute("firstname",firstname);
			model.addAttribute("lastname",lastname);
			return "register";
		}
		if (!password.equals(retype)) {
			model.addAttribute("errorMessage", "The passwords do not match.");
			model.addAttribute("username",username);
			model.addAttribute("firstname",firstname);
			model.addAttribute("lastname",lastname);
			return "register";
		}
		user = new User();
		user.setFirstname(firstname);
		user.setLastname(lastname);
		user.setUsername(username);
		user.setPassword(password);
		users.save(user);
		return "login";
	}
	private boolean ofMinLength(String str, int length) {
		return str != null && str.length() >= length;
	}
}
