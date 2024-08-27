package taco.klkl.domain.image.dto.request;

import jakarta.validation.constraints.NotBlank;
import taco.klkl.global.common.constants.ImageValidationMessages;

public record ProductImageUploadRequest(
	@NotBlank(message = ImageValidationMessages.FILE_EXTENSION_NOT_BLANK)
	String fileExtension
) {
}
