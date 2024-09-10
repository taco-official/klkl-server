package taco.klkl.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import taco.klkl.global.common.constants.UserValidationMessages;

public record UserCreateRequest(
	@NotBlank(message = UserValidationMessages.NAME_NOT_BLANK)
	String name,

	String description
) {
	public static UserCreateRequest of(
		final String name,
		final String description
	) {
		return new UserCreateRequest(name, description);
	}
}
