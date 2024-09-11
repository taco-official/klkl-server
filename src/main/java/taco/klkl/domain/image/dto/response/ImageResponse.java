package taco.klkl.domain.image.dto.response;

import taco.klkl.domain.image.domain.Image;

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
}
