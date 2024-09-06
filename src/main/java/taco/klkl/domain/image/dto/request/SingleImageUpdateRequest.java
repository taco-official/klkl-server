package taco.klkl.domain.image.dto.request;

import jakarta.validation.constraints.NotNull;
import taco.klkl.global.common.constants.ImageValidationMessages;

public record SingleImageUpdateRequest(
	@NotNull(message = ImageValidationMessages.IMAGE_ID_NOT_NULL)
	Long imageId
) {
}
