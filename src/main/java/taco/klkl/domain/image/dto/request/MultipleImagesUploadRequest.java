package taco.klkl.domain.image.dto.request;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import taco.klkl.global.common.constants.ImageValidationMessages;

public record MultipleImagesUploadRequest(
	@NotEmpty(message = ImageValidationMessages.FILE_EXTENSIONS_NOT_EMPTY)
	List<String> fileExtensions
) {
}
