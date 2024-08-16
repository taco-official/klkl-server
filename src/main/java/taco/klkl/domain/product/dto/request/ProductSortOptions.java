package taco.klkl.domain.product.dto.request;

import org.springframework.data.domain.Sort;

public record ProductSortOptions(
	String sortBy,
	String sortDirection
) {
}
