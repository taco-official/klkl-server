package taco.klkl.domain.category.dto.response;

import java.util.List;

import taco.klkl.domain.category.domain.Subcategory;
import taco.klkl.domain.category.domain.SubcategoryFilter;

public record SubcategoryWithFilterResponseDto(
	Long subcategoryId,
	String subcategory,
	List<FilterResponseDto> filters
) {
	public static SubcategoryWithFilterResponseDto from(Subcategory subcategory) {
		return new SubcategoryWithFilterResponseDto(subcategory.getId(), subcategory.getName().getKoreanName(),
			subcategory.getSubcategoryFilters()
				.stream()
				.map(SubcategoryFilter::getFilter)
				.map(FilterResponseDto::from)
				.toList());
	}
}
