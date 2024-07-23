package taco.klkl.domain.user.dto.response;

import taco.klkl.domain.user.domain.User;

public record UserDetailResponseDto(
	Long id,
	String profile,
	String name,
	String description,
	int totalLikeCount
) {
	public static UserDetailResponseDto from(User user) {
		return new UserDetailResponseDto(
			user.getId(),
			user.getProfile(),
			user.getName(),
			user.getDescription(),
			0
		);
	}
}
