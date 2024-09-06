package taco.klkl.domain.image.dto.response;

import java.util.List;

public record MultipleUploadCompleteResponse(
	List<Long> imageIds
) {
	public static MultipleUploadCompleteResponse from(final List<Long> imageIds) {
		return new MultipleUploadCompleteResponse(imageIds);
	}
}
