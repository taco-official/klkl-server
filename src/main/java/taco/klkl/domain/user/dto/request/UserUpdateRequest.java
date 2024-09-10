package taco.klkl.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import taco.klkl.global.common.constants.UserValidationMessages;

public record UserUpdateRequest(
	@NotBlank(message = UserValidationMessages.NAME_NOT_BLANK)
	String name,

	String description
) {
}
