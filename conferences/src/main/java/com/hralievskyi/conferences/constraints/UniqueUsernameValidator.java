package com.hralievskyi.conferences.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hralievskyi.conferences.service.UserService;

/*class is used in UniqueUsername annotation*/

@Component
public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, String> {

	UserService userService;

	@Autowired
	public UniqueUsernameValidator(UserService userService) {
		super();
		this.userService = userService;
	}

	@Override
	public boolean isValid(String username, ConstraintValidatorContext context) {
		return !userService.isUsernameAlreadyExist(username);

	}

}