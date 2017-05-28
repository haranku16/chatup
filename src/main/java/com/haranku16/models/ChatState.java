package com.haranku16.models;

import java.util.LinkedList;
import java.util.List;

public class ChatState {
	private List<Message> forwardMessages = new LinkedList<>();
	private List<Message> backwardMessages = new LinkedList<>();
	private boolean expandingBackward = false;
	private String friend = "";
	private int backward = -1;
	private int forward = -1;
	/**
	 * @return the friend
	 */
	public String getFriend() {
		return friend;
	}
	/**
	 * @param friend the friend to set
	 */
	public void setFriend(String friend) {
		this.friend = friend;
	}
	/**
	 * @return the backward
	 */
	public int getBackward() {
		return backward;
	}
	/**
	 * @param backward the backward to set
	 */
	public void setBackward(int backward) {
		this.backward = backward;
	}
	/**
	 * @return the forward
	 */
	public int getForward() {
		return forward;
	}
	/**
	 * @param forward the forward to set
	 */
	public void setForward(int forward) {
		this.forward = forward;
	}
	/**
	 * @return the expandingForward
	 */
	public boolean isExpandingBackward() {
		return expandingBackward;
	}
	/**
	 * @param expandingForward the expandingForward to set
	 */
	public void setExpandingBackward(boolean expandingForward) {
		this.expandingBackward = expandingForward;
	}
	/**
	 * @return the forwardMessages
	 */
	public List<Message> getForwardMessages() {
		return forwardMessages;
	}
	/**
	 * @param forwardMessages the forwardMessages to set
	 */
	public void setForwardMessages(List<Message> forwardMessages) {
		this.forwardMessages = forwardMessages;
	}
	/**
	 * @return the backwardMessages
	 */
	public List<Message> getBackwardMessages() {
		return backwardMessages;
	}
	/**
	 * @param backwardMessages the backwardMessages to set
	 */
	public void setBackwardMessages(List<Message> backwardMessages) {
		this.backwardMessages = backwardMessages;
	}
}
