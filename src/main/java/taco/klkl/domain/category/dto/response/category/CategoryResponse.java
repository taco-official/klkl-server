package taco.klkl.domain.category.dto.response.category;

import java.util.List;

import taco.klkl.domain.category.domain.category.Category;
import taco.klkl.domain.category.dto.response.subcategory.SubcategoryResponse;

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
			category.getName(),
			subcategories
		);
	}
}
