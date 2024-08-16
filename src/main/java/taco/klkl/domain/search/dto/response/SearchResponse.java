package taco.klkl.domain.search.dto.response;

import java.util.List;

import taco.klkl.domain.category.dto.response.CategoryResponse;
import taco.klkl.domain.category.dto.response.SubcategoryResponse;
import taco.klkl.domain.product.dto.response.ProductSimpleResponse;
import taco.klkl.domain.region.dto.response.CityResponse;
import taco.klkl.domain.region.dto.response.CountrySimpleResponse;

public record SearchResponse(
	List<CountrySimpleResponse> countries,
	List<CityResponse> cities,
	List<CategoryResponse> categories,
	List<SubcategoryResponse> subcategories,
	List<ProductSimpleResponse> products
) {
	public static SearchResponse of(
		final List<CountrySimpleResponse> countries,
		final List<CityResponse> cities,
		final List<CategoryResponse> categories,
		final List<SubcategoryResponse> subcategories,
		final List<ProductSimpleResponse> products
	) {
		return new SearchResponse(countries, cities, categories, subcategories, products);
	}
}
