package taco.klkl.domain.search.dto.response;

import java.util.List;

import taco.klkl.domain.category.dto.response.CategoryResponseDto;
import taco.klkl.domain.category.dto.response.SubcategoryResponseDto;
import taco.klkl.domain.product.dto.response.ProductSimpleResponseDto;
import taco.klkl.domain.region.dto.response.CityResponseDto;
import taco.klkl.domain.region.dto.response.CountrySimpleResponseDto;

public record SearchResponseDto(
	List<CountrySimpleResponseDto> countries,
	List<CityResponseDto> cities,
	List<CategoryResponseDto> categories,
	List<SubcategoryResponseDto> subcategories,
	List<ProductSimpleResponseDto> products
) {
	public static SearchResponseDto of(
		List<CountrySimpleResponseDto> countries,
		List<CityResponseDto> cities,
		List<CategoryResponseDto> categories,
		List<SubcategoryResponseDto> subcategories,
		List<ProductSimpleResponseDto> products
	) {
		return new SearchResponseDto(countries, cities, categories, subcategories, products);
	}
}
