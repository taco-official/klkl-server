package taco.klkl.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import taco.klkl.global.common.constants.UserValidationMessages;

public record UserUpdateRequest(
	@NotBlank(message = UserValidationMessages.NAME_NOT_BLANK)
	String name,

	@NotBlank(message = UserValidationMessages.GENDER_NOT_BLANK)
	String gender,

	@PositiveOrZero(message = UserValidationMessages.AGE_POSITIVE_OR_ZERO)
	Integer age,

	String description
) {
}
