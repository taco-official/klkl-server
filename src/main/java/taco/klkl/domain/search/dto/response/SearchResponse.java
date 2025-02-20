package taco.klkl.domain.search.dto.response;

import java.util.List;

import taco.klkl.domain.category.dto.response.category.CategorySimpleResponse;
import taco.klkl.domain.category.dto.response.subcategory.SubcategorySimpleResponse;
import taco.klkl.domain.region.dto.response.city.CitySimpleResponse;
import taco.klkl.domain.region.dto.response.country.CountrySimpleResponse;

public record SearchResponse(
	List<CountrySimpleResponse> countries,
	List<CitySimpleResponse> cities,
	List<CategorySimpleResponse> categories,
	List<SubcategorySimpleResponse> subcategories
) {
	public static SearchResponse of(
		final List<CountrySimpleResponse> countries,
		final List<CitySimpleResponse> cities,
		final List<CategorySimpleResponse> categories,
		final List<SubcategorySimpleResponse> subcategories
	) {
		return new SearchResponse(countries, cities, categories, subcategories);
	}
}
