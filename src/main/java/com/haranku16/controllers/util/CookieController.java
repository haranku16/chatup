package com.haranku16.controllers.util;

import javax.servlet.http.Cookie;

import com.haranku16.models.User;
import com.haranku16.repositories.UserRepository;

public class CookieController {
	
	private static CookieController instance = new CookieController();
	
	private CookieController() {}
	
	public static CookieController getInstance() {
		return instance;
	}
	
	public User getUser(Cookie[] cookies, UserRepository users) {
		String username = null, session = null;
		if (cookies == null) return null;
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals("username")) username = cookie.getValue();
			if (cookie.getName().equals("session")) session = cookie.getValue();
		}
		if (username != null && session != null) {
			User user = users.findByUsername(username);
			if (user == null) {
				return null;
			}
			if (user.getSessions().contains(session)) {
				return user;
			}
			return null;
		}
		return null;
	}
}