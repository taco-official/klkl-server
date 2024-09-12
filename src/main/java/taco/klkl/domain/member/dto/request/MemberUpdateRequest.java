package taco.klkl.domain.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import taco.klkl.global.common.constants.MemberValidationMessages;

public record MemberUpdateRequest(
	@NotBlank(message = MemberValidationMessages.NAME_NOT_BLANK)
	String name,

	String description
) {
}
