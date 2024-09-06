package taco.klkl.domain.image.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import taco.klkl.global.common.constants.ImageValidationMessages;

public record SingleImageUploadRequest(
	@NotNull(message = ImageValidationMessages.FILE_EXTENSION_NOT_NULL)
	String fileExtension
) {
}
