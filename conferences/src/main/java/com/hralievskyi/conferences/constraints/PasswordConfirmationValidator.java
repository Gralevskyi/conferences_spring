package com.hralievskyi.conferences.constraints;

import java.lang.reflect.Method;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/*class for PasswordConfirmingCheck annotation*/

public class PasswordConfirmationValidator implements ConstraintValidator<PasswordConfirmationCheck, Object> {

	private String firstFieldName;
	private String secondFieldName;
	private String message;

	@Override
	public void initialize(final PasswordConfirmationCheck constraintAnnotation) {
		firstFieldName = constraintAnnotation.first();
		secondFieldName = constraintAnnotation.second();
		message = constraintAnnotation.message();
	}

	@Override
	public boolean isValid(final Object value, final ConstraintValidatorContext context) {
		boolean valid = true;
		try {
			String getFirstFieldName = "get" + firstFieldName.substring(0, 1).toUpperCase() + firstFieldName.substring(1);
			Method getFirstField = value.getClass().getMethod(getFirstFieldName);
			final Object firstObj = getFirstField.invoke(value);

			String getSecondFieldName = "get" + secondFieldName.substring(0, 1).toUpperCase() + secondFieldName.substring(1);
			Method getSecondField = value.getClass().getMethod(getSecondFieldName);
			final Object secondObj = getSecondField.invoke(value);

			valid = firstObj == null && secondObj == null || firstObj != null && firstObj.equals(secondObj);
		} catch (final Exception ignore) {
			// ignore
		}

		if (!valid) {
			context.buildConstraintViolationWithTemplate(message).addPropertyNode(firstFieldName).addConstraintViolation().disableDefaultConstraintViolation();
		}

		return valid;
	}
}
