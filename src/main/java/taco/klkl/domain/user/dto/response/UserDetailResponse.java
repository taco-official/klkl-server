package taco.klkl.domain.user.dto.response;

import taco.klkl.domain.user.domain.User;

public record UserDetailResponse(
	Long id,
	String profileImageUrl,
	String name,
	String description,
	int totalLikeCount
) {
	public static UserDetailResponse from(final User user) {
		return new UserDetailResponse(
			user.getId(),
			user.getProfileImageUrl(),
			user.getName(),
			user.getDescription(),
			0
		);
	}
}
