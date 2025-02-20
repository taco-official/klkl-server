package taco.klkl.domain.image.dto.response;

import taco.klkl.domain.image.domain.Image;

public record PresignedUrlResponse(
	Long id,
	String presignedUrl
) {
	public static PresignedUrlResponse of(final Image image, final String presignedUrl) {
		return new PresignedUrlResponse(
			image.getId(),
			presignedUrl
		);
	}
}
