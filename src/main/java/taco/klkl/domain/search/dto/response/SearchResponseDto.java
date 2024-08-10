package taco.klkl.domain.search.dto.response;

import java.util.List;

import taco.klkl.domain.category.dto.response.CategoryResponseDto;
import taco.klkl.domain.category.dto.response.SubcategoryResponseDto;
import taco.klkl.domain.region.dto.response.CityResponseDto;
import taco.klkl.domain.region.dto.response.CountrySimpleResponseDto;

public record SearchResponseDto(
	List<CountrySimpleResponseDto> countries,
	List<CityResponseDto> cities,
	List<CategoryResponseDto> categories,
	List<SubcategoryResponseDto> subcategories
) {
	public static SearchResponseDto of(
		List<CountrySimpleResponseDto> countries,
		List<CityResponseDto> cities,
		List<CategoryResponseDto> categories,
		List<SubcategoryResponseDto> subcategories
	) {
		return new SearchResponseDto(countries, cities, categories, subcategories);
	}
}
