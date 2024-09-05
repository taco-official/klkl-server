package taco.klkl.domain.user.dto.response;

import taco.klkl.domain.user.domain.User;
import taco.klkl.global.util.UserUtil;

public record UserDetailResponse(
	Long id,
	String profileUrl,
	String name,
	String description,
	int totalLikeCount
) {
	public static UserDetailResponse from(final User user) {
		return new UserDetailResponse(
			user.getId(),
			UserUtil.generateProfileUrlByUser(user),
			user.getName(),
			user.getDescription(),
			0
		);
	}
}
