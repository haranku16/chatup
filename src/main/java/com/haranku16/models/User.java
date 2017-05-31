package com.haranku16.models;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.data.annotation.Id;

public class User {
	@Id
	private String id;
	private String username = "";
	private String password = "";
	private String firstname = "";
	private String lastname = "";
	private Set<String> requests = new HashSet<String>();
	private Set<String> sessions = new HashSet<String>();
	private Map<String,String> chats = new HashMap<String,String>();
	/**
	 * @return the sessions
	 */
	public Set<String> getSessions() {
		return sessions;
	}
	/**
	 * @param sessions the sessions to set
	 */
	public void setSessions(Set<String> sessions) {
		this.sessions = sessions;
	}
	/**
	 * @return the full name
	 */
	public String getFullname() {
		return firstname + " " + lastname;
	}
	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @return the firstname
	 */
	public String getFirstname() {
		return firstname;
	}
	/**
	 * @param firstname the firstname to set
	 */
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	/**
	 * @return the lastname
	 */
	public String getLastname() {
		return lastname;
	}
	/**
	 * @param lastname the lastname to set
	 */
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	/**
	 * @return the requests
	 */
	public Set<String> getRequests() {
		return requests;
	}
	/**
	 * @param requests the requests to set
	 */
	public void setRequests(Set<String> requests) {
		this.requests = requests;
	}
	/**
	 * @return the chats
	 */
	public Map<String, String> getChats() {
		return chats;
	}
	/**
	 * @param chats the chats to set
	 */
	public void setChats(Map<String, String> chats) {
		this.chats = chats;
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
