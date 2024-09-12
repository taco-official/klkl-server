package taco.klkl.domain.user.dto.response;

import taco.klkl.domain.image.dto.response.ImageResponse;
import taco.klkl.domain.user.domain.User;

public record UserDetailResponse(
	Long id,
	ImageResponse image,
	String name,
	String description
) {
	public static UserDetailResponse from(final User user) {
		return new UserDetailResponse(
			user.getId(),
			ImageResponse.from(user.getImage()),
			user.getName(),
			user.getDescription()
		);
	}
}
