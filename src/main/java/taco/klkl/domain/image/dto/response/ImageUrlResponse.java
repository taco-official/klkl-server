package taco.klkl.domain.image.dto.response;

public record ImageUrlResponse(
	String imageUrl
) {
	public static ImageUrlResponse from(final String imageUrl) {
		return new ImageUrlResponse(imageUrl);
	}
}
