package taco.klkl.domain.user.dto.response;

import taco.klkl.domain.user.domain.User;

public record UserSimpleResponseDto(
	Long id,
	String profile,
	String name
) {
	public static UserSimpleResponseDto from(User user) {
		return new UserSimpleResponseDto(
			user.getId(),
			user.getProfile(),
			user.getName()
		);
	}
}
