package taco.klkl.domain.user.dto.request;

import jakarta.validation.constraints.NotNull;
import taco.klkl.global.common.constants.UserValidationMessages;

public record UserFollowRequest(
	@NotNull(message = UserValidationMessages.TARGET_USER_ID_NOT_NULL)
	Long targetUserId
) {
}
