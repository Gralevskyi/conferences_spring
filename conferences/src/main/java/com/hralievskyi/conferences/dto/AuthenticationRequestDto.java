package com.hralievskyi.conferences.dto;

public class AuthenticationRequestDto {
	private String login;
	private String password;

	public String getLogin() {
		return login;
	}

	public void setLogin(String username) {
		this.login = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
