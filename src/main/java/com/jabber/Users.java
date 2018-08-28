package com.jabber;

public class Users
{
	private String id, username;

	public Users(String id, String username) {
		this.id = id;
		this.username = username;
	}

	public Users() {

	}

	public String getId() {
		return(id);
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return(username);
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
