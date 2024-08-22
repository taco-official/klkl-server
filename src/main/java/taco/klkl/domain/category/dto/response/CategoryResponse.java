package taco.klkl.domain.category.dto.response;

import java.util.List;

import taco.klkl.domain.category.domain.Category;

public record CategoryResponse(
	Long id,
	String name,
	List<SubcategoryResponse> subcategories
) {
	public static CategoryResponse from(final Category category) {
		List<SubcategoryResponse> subcategories = category.getSubcategories().stream()
			.map(SubcategoryResponse::from)
			.toList();

		return new CategoryResponse(
			category.getId(),
			category.getName().getKoreanName(),
			subcategories
		);
	}
}
