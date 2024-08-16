package taco.klkl.domain.user.dto.response;

import taco.klkl.domain.user.domain.User;

public record UserDetailResponse(
	Long id,
	String profile,
	String name,
	String description,
	int totalLikeCount
) {
	public static UserDetailResponse from(User user) {
		return new UserDetailResponse(
			user.getId(),
			user.getProfile(),
			user.getName(),
			user.getDescription(),
			0
		);
	}
}
