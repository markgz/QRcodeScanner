package com.pccw.qrcode.dao;

import java.util.List;

public class UserInfo {

	private String id;
	private String username;
	private String email;
	private String phone;
	private List<String> votes;
	private List<String> likes;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public List<String> getVotes() {
		return votes;
	}

	public void setVotes(List<String> votes) {
		this.votes = votes;
	}

	public List<String> getLikes() {
		return likes;
	}

	public void setLikes(List<String> likes) {
		this.likes = likes;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "#id: " + getId() + "\nusername: " + getUsername() + "\nemail: " + getEmail() + "\nphone:" + getPhone() + "\nvotes:" + getVotes() + "\nlikes:" + getLikes();
	}

}
