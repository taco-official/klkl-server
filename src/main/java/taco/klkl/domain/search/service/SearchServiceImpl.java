package taco.klkl.domain.search.service;

import java.util.List;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import taco.klkl.domain.category.dto.response.category.CategoryResponse;
import taco.klkl.domain.category.dto.response.subcategory.SubcategoryResponse;
import taco.klkl.domain.category.service.category.CategoryService;
import taco.klkl.domain.category.service.subcategory.SubcategoryService;
import taco.klkl.domain.region.dto.response.city.CityResponse;
import taco.klkl.domain.region.dto.response.country.CountrySimpleResponse;
import taco.klkl.domain.region.service.city.CityService;
import taco.klkl.domain.region.service.country.CountryService;
import taco.klkl.domain.search.dto.response.SearchResponse;

@Slf4j
@Primary
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {

	private final CountryService countryService;
	private final CityService cityService;
	private final CategoryService categoryService;
	private final SubcategoryService subcategoryService;

	@Override
	public SearchResponse findSearchResult(final String queryParam) {
		final List<CountrySimpleResponse> countries = findCountriesByQueryParam(queryParam);
		final List<CityResponse> cities = findCitiesByQueryParam(queryParam);
		final List<CategoryResponse> categories = findCategoriesByQueryParam(queryParam);
		final List<SubcategoryResponse> subcategories = findSubcategoriesByQueryParam(queryParam);
		return SearchResponse.of(countries, cities, categories, subcategories);
	}

	private List<CountrySimpleResponse> findCountriesByQueryParam(final String queryParam) {
		return countryService.findAllCountriesByPartialString(queryParam);
	}

	private List<CityResponse> findCitiesByQueryParam(final String queryParam) {
		return cityService.findAllCitiesByPartialString(queryParam);
	}

	private List<CategoryResponse> findCategoriesByQueryParam(final String queryParam) {
		return categoryService.findAllCategoriesByPartialString(queryParam);
	}

	private List<SubcategoryResponse> findSubcategoriesByQueryParam(final String queryParam) {
		return subcategoryService.findAllSubcategoriesByPartialString(queryParam);
	}

}
