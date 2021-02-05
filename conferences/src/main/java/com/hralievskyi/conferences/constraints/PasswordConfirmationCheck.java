package com.hralievskyi.conferences.constraints;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/* annotation created with purpose to check if passwords confirmation match with password during registration
 * for creating annotation and PasswordConfirmationClass used information from 
 * https://www.baeldung.com/spring-mvc-custom-validator
 * https://memorynotfound.com/field-matching-bean-validation-annotation-example/
 * */

@Documented
@Constraint(validatedBy = PasswordConfirmationValidator.class)
@Target({ ElementType.TYPE, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordConfirmationCheck {
	String message() default "The fields must match";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	String first();

	String second();
}
