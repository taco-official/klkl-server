package taco.klkl.domain.category.dto.response.subcategory;

import taco.klkl.domain.category.domain.subcategory.Subcategory;

public record SubcategorySimpleResponse(
	Long id,
	String name
) {
	public static SubcategorySimpleResponse from(final Subcategory subcategory) {
		return new SubcategorySimpleResponse(
			subcategory.getId(),
			subcategory.getName()
		);
	}
}
