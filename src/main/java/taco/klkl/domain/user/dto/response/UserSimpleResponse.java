package taco.klkl.domain.user.dto.response;

import taco.klkl.domain.user.domain.User;

public record UserSimpleResponse(
	Long id,
	String profile,
	String name
) {
	public static UserSimpleResponse from(User user) {
		return new UserSimpleResponse(
			user.getId(),
			user.getProfile(),
			user.getName()
		);
	}
}
