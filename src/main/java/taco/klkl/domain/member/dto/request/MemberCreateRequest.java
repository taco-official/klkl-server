package taco.klkl.domain.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import taco.klkl.global.common.constants.MemberValidationMessages;

public record MemberCreateRequest(
	@NotBlank(message = MemberValidationMessages.NAME_NOT_BLANK)
	String name,

	String description
) {
	public static MemberCreateRequest of(
		final String name,
		final String description
	) {
		return new MemberCreateRequest(name, description);
	}
}
