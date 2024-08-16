package taco.klkl.domain.category.dto.response;

import taco.klkl.domain.category.domain.Subcategory;

public record SubcategoryResponse(
	Long subcategoryId,
	String subcategory
) {
	public static SubcategoryResponse from(Subcategory subcategory) {
		return new SubcategoryResponse(subcategory.getId(), subcategory.getName().getKoreanName());
	}
}
