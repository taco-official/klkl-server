package taco.klkl.domain.user.dto.response;

import taco.klkl.domain.user.domain.User;

public record UserSimpleResponse(
	Long id,
	String profileImageUrl,
	String name
) {
	public static UserSimpleResponse from(final User user) {
		return new UserSimpleResponse(
			user.getId(),
			user.getProfileImageUrl(),
			user.getName()
		);
	}
}
