package taco.klkl.domain.category.dto.response;

import java.util.List;

import taco.klkl.domain.category.domain.Subcategory;
import taco.klkl.domain.category.domain.SubcategoryFilter;

public record FilterWithSubcategoryResponse(
	Long subcategoryId,
	String subcategory,
	List<FilterResponse> filters
) {
	public static FilterWithSubcategoryResponse from(Subcategory subcategory) {
		return new FilterWithSubcategoryResponse(subcategory.getId(), subcategory.getName().getKoreanName(),
			subcategory.getSubcategoryFilters()
				.stream()
				.map(SubcategoryFilter::getFilter)
				.map(FilterResponse::from)
				.toList());
	}
}
