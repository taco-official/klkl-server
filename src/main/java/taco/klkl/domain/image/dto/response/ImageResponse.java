package taco.klkl.domain.image.dto.response;

import taco.klkl.domain.image.domain.Image;
import taco.klkl.domain.member.domain.profile.ProfileImage;

public record ImageResponse(
	Long id,
	String url
) {
	public static ImageResponse from(final Image image) {
		if (image == null) {
			return null;
		}
		return new ImageResponse(image.getId(), image.getUrl());
	}

	public static ImageResponse from(final ProfileImage profileImage) {
		if (profileImage == null) {
			return null;
		}
		return new ImageResponse(profileImage.getImageId(), profileImage.getUrl());
	}
}
