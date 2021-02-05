package com.hralievskyi.conferences.constraints;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/*check if a login is already exist in DataBase*/

@Constraint(validatedBy = UniqueUsernameValidator.class)
@Retention(RUNTIME)
@Target(FIELD)
public @interface UniqueUsername {
	String message() default "There is already a user with this login";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
