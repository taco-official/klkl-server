package taco.klkl.domain.category.dto.response.subcategory;

import taco.klkl.domain.category.domain.category.Category;
import taco.klkl.domain.category.domain.subcategory.Subcategory;

public record SubcategoryHierarchyResponse(
	Long subcategoryId,
	Long categoryId
) {
	public static SubcategoryHierarchyResponse from(final Subcategory subcategory) {
		final Category category = subcategory.getCategory();

		return new SubcategoryHierarchyResponse(
			subcategory.getId(),
			category.getId()
		);
	}
}
