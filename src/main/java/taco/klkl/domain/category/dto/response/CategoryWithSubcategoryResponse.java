package taco.klkl.domain.category.dto.response;

import java.util.List;

import taco.klkl.domain.category.domain.Category;

public record CategoryWithSubcategoryResponse(
	Long categoryId,
	String category,
	List<SubcategoryResponse> subcategories
) {
	public static CategoryWithSubcategoryResponse from(Category category) {
		return new CategoryWithSubcategoryResponse(category.getId(), category.getName().getKoreanName(),
			category.getSubcategories()
				.stream()
				.map(SubcategoryResponse::from)
				.toList());
	}
}
