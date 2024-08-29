package taco.klkl.domain.category.dto.response;

import taco.klkl.domain.category.domain.Subcategory;

public record SubcategoryResponse(
	Long id,
	String name
) {
	public static SubcategoryResponse from(final Subcategory subcategory) {
		return new SubcategoryResponse(subcategory.getId(), subcategory.getName());
	}
}
