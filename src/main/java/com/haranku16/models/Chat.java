package com.haranku16.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

import org.springframework.data.annotation.Id;

public class Chat {
	@Id
	private String id;
	private Set<String> users = new HashSet<String>();
	private List<Message> messages = new ArrayList<Message>();
	/**
	 * @return the users
	 */
	public Set<String> getUsers() {
		return users;
	}
	/**
	 * @param users the users to set
	 */
	public void setUsers(Set<String> users) {
		this.users = users;
	}
	/**
	 * @return the messages
	 */
	public List<Message> getMessages() {
		return messages;
	}
	/**
	 * @param messages the messages to set
	 */
	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
}
