package com.haranku16.controllers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.haranku16.controllers.util.CookieController;
import com.haranku16.models.Message;
import com.haranku16.models.User;
import com.haranku16.repositories.ChatRepository;
import com.haranku16.repositories.UserRepository;

@Controller
public class HomeController {
	public static class FriendDateTuple {
		private User friend;
		private String date;
		/**
		 * @return the friend
		 */
		public User getFriend() {
			return friend;
		}
		/**
		 * @param friend the friend to set
		 */
		public void setFriend(User friend) {
			this.friend = friend;
		}
		/**
		 * @return the date
		 */
		public String getDate() {
			return date;
		}
		/**
		 * @param date the date to set
		 */
		public void setDate(String date) {
			this.date = date;
		}
	}
	
	@Autowired
	private UserRepository users;
	
	@Autowired
	private ChatRepository chats;
	
	@RequestMapping(path="/", method=RequestMethod.GET)
	public String home(Model model, HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		User user = CookieController.getInstance().getUser(cookies, users);
		if (user == null) {
			return "login";
		}
		model.addAttribute("user", user);
		List<FriendDateTuple> tuples = user.getChats().keySet().stream()
				.map(username->{
					FriendDateTuple tuple = new FriendDateTuple();
					tuple.setFriend(users.findByUsername(username));
					tuple.setDate(mostRecent(user,tuple.getFriend()));
					return tuple;
				}).collect(Collectors.toList());
		tuples.sort((t1,t2)->t1.getFriend().getFullname().compareTo(t2.getFriend().getFullname()));
		model.addAttribute("friendDateTuples", tuples);
		return "home";
	}
	
	public String mostRecent(User user, User friend) {
		if (friend == null) return "No messages";
		String chatId = user.getChats().get(friend.getUsername());
		List<Message> messages = chats.findById(chatId).getMessages();
		if (messages.isEmpty()) return "No messages";
		else {
			Date d = messages.get(messages.size()-1).getDate();
			SimpleDateFormat format = new SimpleDateFormat("EEEE, MMMM d, YYYY h:mm:ss a");
			return "Last message sent at " + format.format(d);
		}
	}
}
