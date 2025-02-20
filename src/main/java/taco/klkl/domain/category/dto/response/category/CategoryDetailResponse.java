package taco.klkl.domain.category.dto.response.category;

import java.util.List;

import taco.klkl.domain.category.domain.category.Category;
import taco.klkl.domain.category.dto.response.subcategory.SubcategoryDetailResponse;

public record CategoryDetailResponse(
	Long id,
	String name,
	List<SubcategoryDetailResponse> subcategories
) {
	public static CategoryDetailResponse from(final Category category) {
		List<SubcategoryDetailResponse> subcategories = category.getSubcategories().stream()
			.map(SubcategoryDetailResponse::from)
			.toList();

		return new CategoryDetailResponse(
			category.getId(),
			category.getName(),
			subcategories
		);
	}
}
