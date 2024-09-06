package taco.klkl.domain.image.dto.request;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import taco.klkl.global.common.constants.ImageValidationMessages;

public record MultipleImagesUpdateRequest(
	@NotEmpty(message = ImageValidationMessages.IMAGE_IDS_NOT_EMPTY)
	List<Long> imageIds
) {
}
