package taco.klkl.domain.image.dto.request;

import java.util.List;

public record MultipleImagesUpdateRequest(
	List<Long> imageIds
) {
}
