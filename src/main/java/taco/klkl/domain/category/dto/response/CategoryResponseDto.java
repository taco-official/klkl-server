package taco.klkl.domain.category.dto.response;

import taco.klkl.domain.category.domain.Category;

public record CategoryResponseDto(
	Long categoryId,
	String category
) {
	public static CategoryResponseDto from(Category category) {
		return new CategoryResponseDto(category.getId(), category.getName().getName());
	}
}
