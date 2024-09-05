package taco.klkl.domain.image.dto.request;

import java.util.List;

public record MultipleImagesUploadRequest(
	// TODO: 유효성 검사 필요
	List<String> fileExtensions
) {
}
