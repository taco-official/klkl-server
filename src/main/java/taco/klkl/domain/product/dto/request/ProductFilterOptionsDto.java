package taco.klkl.domain.product.dto.request;

import java.util.List;

public record ProductFilterOptionsDto(
	List<Long> cityIds,
	List<Long> subcategoryIds,
	List<Long> filterIds
) {
}
