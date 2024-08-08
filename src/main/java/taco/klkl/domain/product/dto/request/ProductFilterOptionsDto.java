package taco.klkl.domain.product.dto.request;

import java.util.List;

public record ProductFilterOptionsDto(
	List<Long> countryIds
) {
}
