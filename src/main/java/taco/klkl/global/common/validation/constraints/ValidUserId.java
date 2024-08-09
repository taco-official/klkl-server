package taco.klkl.global.common.validation.constraints;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import taco.klkl.global.common.constants.CommentValidationMessages;
import taco.klkl.global.common.validation.UserIdValidator;

@Documented
@Constraint(validatedBy = UserIdValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidUserId {
	String message() default CommentValidationMessages.USER_ID_INVALID;

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
