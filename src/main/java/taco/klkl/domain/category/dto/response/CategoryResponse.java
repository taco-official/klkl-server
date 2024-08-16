package taco.klkl.domain.category.dto.response;

import taco.klkl.domain.category.domain.Category;

public record CategoryResponse(
	Long id,
	String name
) {
	public static CategoryResponse from(final Category category) {
		return new CategoryResponse(category.getId(), category.getName().getKoreanName());
	}
}
