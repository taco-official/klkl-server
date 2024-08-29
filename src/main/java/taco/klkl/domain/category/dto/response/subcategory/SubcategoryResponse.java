package taco.klkl.domain.category.dto.response.subcategory;

import taco.klkl.domain.category.domain.subcategory.Subcategory;

public record SubcategoryResponse(
	Long id,
	String name
) {
	public static SubcategoryResponse from(final Subcategory subcategory) {
		return new SubcategoryResponse(subcategory.getId(), subcategory.getName());
	}
}
