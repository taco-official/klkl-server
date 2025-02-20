package taco.klkl.domain.category.dto.response.category;

import taco.klkl.domain.category.domain.category.Category;

public record CategorySimpleResponse(
	Long id,
	String name
) {
	public static CategorySimpleResponse from(final Category category) {
		return new CategorySimpleResponse(
			category.getId(),
			category.getName()
		);
	}
}
