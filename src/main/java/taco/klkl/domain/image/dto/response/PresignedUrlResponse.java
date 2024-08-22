package taco.klkl.domain.image.dto.response;

public record PresignedUrlResponse(
	String presignedUrl
) {
	public static PresignedUrlResponse from(final String presignedUrl) {
		return new PresignedUrlResponse(presignedUrl);
	}
}
