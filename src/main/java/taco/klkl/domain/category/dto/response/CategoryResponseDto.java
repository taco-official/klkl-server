package taco.klkl.domain.category.dto.response;

import taco.klkl.domain.category.domain.CategoryName;

public record CategoryResponseDto(
	Long categoryId,
	String category
) {
	public static CategoryResponseDto of(Long id, CategoryName categoryName) {
		return new CategoryResponseDto(id, categoryName.getName());
	}
}
