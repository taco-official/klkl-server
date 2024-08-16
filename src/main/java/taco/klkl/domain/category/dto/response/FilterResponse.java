package taco.klkl.domain.category.dto.response;

import taco.klkl.domain.category.domain.Filter;

public record FilterResponse(
	Long id,
	String name
) {
	public static FilterResponse from(Filter filter) {
		return new FilterResponse(filter.getId(), filter.getName().getKoreanName());
	}
}
