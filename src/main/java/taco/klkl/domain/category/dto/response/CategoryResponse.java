package taco.klkl.domain.category.dto.response;

import taco.klkl.domain.category.domain.Category;

public record CategoryResponse(
	Long categoryId,
	String category
) {
	public static CategoryResponse from(Category category) {
		return new CategoryResponse(category.getId(), category.getName().getKoreanName());
	}
}
