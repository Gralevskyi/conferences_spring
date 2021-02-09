package com.hralievskyi.conferences.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

import com.hralievskyi.conferences.constraints.PasswordConfirmationCheck;
import com.hralievskyi.conferences.constraints.UniqueUsername;
import com.hralievskyi.conferences.entity.user.User;
import com.sun.istack.NotNull;

/*domain class for registration form of a new user*/

@PasswordConfirmationCheck(first = "passwordConfirmation", second = "password")
public class RegistrationForm {

	@Email(message = "{regist.name.validation}")
	@UniqueUsername
	private String username;

	@NotNull
	@Size(min = 5, max = 25, message = "regist.password.validation")
	private String password;
	private String passwordConfirmation;

	public User toUser() {
		return new User(username, password);
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPasswordConfirmation() {
		return passwordConfirmation;
	}

	public void setPasswordConfirmation(String passwordConfirmation) {
		this.passwordConfirmation = passwordConfirmation;
	}

}
