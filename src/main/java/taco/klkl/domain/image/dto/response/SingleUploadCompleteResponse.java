package taco.klkl.domain.image.dto.response;

public record SingleUploadCompleteResponse(
	Long imageId
) {
	public static SingleUploadCompleteResponse from(final Long imageId) {
		return new SingleUploadCompleteResponse(imageId);
	}
}
