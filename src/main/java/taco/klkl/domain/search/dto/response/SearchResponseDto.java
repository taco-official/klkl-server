package taco.klkl.domain.search.dto.response;

import java.util.List;

import taco.klkl.domain.category.dto.response.CategoryResponse;
import taco.klkl.domain.category.dto.response.SubcategoryResponse;
import taco.klkl.domain.product.dto.response.ProductSimpleResponse;
import taco.klkl.domain.region.dto.response.CityResponse;
import taco.klkl.domain.region.dto.response.CountrySimpleResponse;

public record SearchResponseDto(
	List<CountrySimpleResponse> countries,
	List<CityResponse> cities,
	List<CategoryResponse> categories,
	List<SubcategoryResponse> subcategories,
	List<ProductSimpleResponse> products
) {
	public static SearchResponseDto of(
		List<CountrySimpleResponse> countries,
		List<CityResponse> cities,
		List<CategoryResponse> categories,
		List<SubcategoryResponse> subcategories,
		List<ProductSimpleResponse> products
	) {
		return new SearchResponseDto(countries, cities, categories, subcategories, products);
	}
}
