package taco.klkl.domain.user.dto.response;

import taco.klkl.domain.user.domain.User;
import taco.klkl.global.util.UserUtil;

public record UserSimpleResponse(
	Long id,
	String profileUrl,
	String name
) {
	public static UserSimpleResponse from(final User user) {
		return new UserSimpleResponse(
			user.getId(),
			UserUtil.generateProfileUrlByUser(user),
			user.getName()
		);
	}
}
