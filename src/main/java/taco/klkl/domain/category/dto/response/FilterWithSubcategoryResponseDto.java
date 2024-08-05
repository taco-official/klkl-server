package taco.klkl.domain.category.dto.response;

import java.util.List;

import taco.klkl.domain.category.domain.Subcategory;
import taco.klkl.domain.category.domain.SubcategoryFilter;

public record FilterWithSubcategoryResponseDto(
	Long subcategoryId,
	String subcategory,
	List<FilterResponseDto> filters
) {
	public static FilterWithSubcategoryResponseDto from(Subcategory subcategory) {
		return new FilterWithSubcategoryResponseDto(subcategory.getId(), subcategory.getName().getKoreanName(),
			subcategory.getSubcategoryFilters()
				.stream()
				.map(SubcategoryFilter::getFilter)
				.map(FilterResponseDto::from)
				.toList());
	}
}
