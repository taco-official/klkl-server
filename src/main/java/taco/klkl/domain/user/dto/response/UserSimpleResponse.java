package taco.klkl.domain.user.dto.response;

import taco.klkl.domain.image.dto.response.ImageResponse;
import taco.klkl.domain.user.domain.User;

public record UserSimpleResponse(
	Long id,
	ImageResponse image,
	String name
) {
	public static UserSimpleResponse from(final User user) {
		return new UserSimpleResponse(
			user.getId(),
			ImageResponse.from(user.getImage()),
			user.getName()
		);
	}
}
