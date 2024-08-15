package taco.klkl.domain.product.dto.request;

import java.util.Set;

public record ProductFilterOptionsDto(
	Set<Long> cityIds,
	Set<Long> subcategoryIds,
	Set<Long> filterIds
) {
}
