package taco.klkl.domain.image.dto.request;

import jakarta.validation.constraints.NotNull;
import taco.klkl.domain.image.domain.FileExtension;
import taco.klkl.global.common.constants.ImageValidationMessages;

public record ImageCreateRequest(
	@NotNull(message = ImageValidationMessages.IMAGE_FILE_EXTENSION_NOT_NULL)
	FileExtension fileExtension
) {
}
