package com.haranku16.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.haranku16.controllers.util.CookieController;
import com.haranku16.models.Chat;
import com.haranku16.models.ChatState;
import com.haranku16.models.Message;
import com.haranku16.models.User;
import com.haranku16.repositories.ChatRepository;
import com.haranku16.repositories.UserRepository;

@RestController
public class MessageController {
	@Autowired
	private UserRepository users;
	@Autowired
	private ChatRepository chats;
	
	@RequestMapping(path="/messages", method=RequestMethod.POST)
	public ChatState pull(@RequestBody ChatState state,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		User user = CookieController.getInstance().getUser(request.getCookies(), users);
		if (user == null) return setErrorCode(response, "User is null.");
		if (!user.getChats().containsKey(state.getFriend())) return setErrorCode(response, "User is not friends with _.");
		String chatId = user.getChats().get(state.getFriend());
		Chat chat = chats.findById(chatId);
		if (chat == null) return setErrorCode(response, "Chat does not exist.");
		List<Message> forwardMessages = new ArrayList<Message>();
		List<Message> backwardMessages = new ArrayList<Message>();
		if (state.getBackward() == -1) state.setBackward(chat.getMessages().size()-11);
		if (state.getBackward() < 0) state.setBackward(0);
		if (state.getForward() > chat.getMessages().size()-1) return setErrorCode(response, "Forward out of range.");
		if (state.isExpandingBackward()) {
			int newBackward = state.getBackward() - 10;
			if (newBackward < 0) newBackward = 0;
			for (int i = newBackward; i < state.getBackward(); i++) {
				backwardMessages.add(chat.getMessages().get(i));
			}
			state.setBackward(newBackward);
			state.setBackwardMessages(backwardMessages);
		}
		int newForward = chat.getMessages().size()-1;
		for (int i = state.getForward() + 1; i <= newForward; i++) {
			forwardMessages.add(chat.getMessages().get(i));
			System.out.println("Sending message " + chat.getMessages().get(i));
		}
		state.setForward(newForward);
		state.setForwardMessages(forwardMessages);
		return state;
	}
	
	@RequestMapping(path="/message", method=RequestMethod.POST)
	public ChatState post(@RequestBody ChatState state,
			HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User user = CookieController.getInstance().getUser(request.getCookies(), users);
		if (user == null) return setErrorCode(response, "User does not exist.");
		if (state.getForwardMessages().size() != 1) return setErrorCode(response, "Forward out of range.");
		if (!user.getChats().containsKey(state.getFriend())) return setErrorCode(response, "User is not friends with _");
		Chat chat = chats.findById(user.getChats().get(state.getFriend()));
		if (chat == null) return setErrorCode(response, "Chat does not exist.");
		Message posting = state.getForwardMessages().get(0);
		posting.setDate(new Date());
		chat.getMessages().add(posting);
		chats.save(chat);
		return state;
	}
	
	public ChatState setErrorCode(HttpServletResponse response, String msg) throws IOException {
		response.sendError(500, msg);
		return null;
	}
}
