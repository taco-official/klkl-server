package taco.klkl.domain.search.service;

import java.util.List;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import taco.klkl.domain.category.domain.CategoryName;
import taco.klkl.domain.category.domain.SubcategoryName;
import taco.klkl.domain.category.dto.response.CategoryResponse;
import taco.klkl.domain.category.dto.response.SubcategoryResponse;
import taco.klkl.domain.category.service.CategoryService;
import taco.klkl.domain.category.service.SubcategoryService;
import taco.klkl.domain.product.dto.response.ProductSimpleResponse;
import taco.klkl.domain.product.service.ProductService;
import taco.klkl.domain.region.dto.response.CityResponse;
import taco.klkl.domain.region.dto.response.CountrySimpleResponse;
import taco.klkl.domain.region.domain.CityType;
import taco.klkl.domain.region.domain.CountryType;
import taco.klkl.domain.region.service.CityService;
import taco.klkl.domain.region.service.CountryService;
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
	private final ProductService productService;

	@Override
	public SearchResponse getSearchResult(final String queryParam) {

		final List<CountrySimpleResponse> findCountries = getCountriesByQueryParam(queryParam);
		final List<CityResponse> findCities = getCitiesByQueryParam(queryParam);
		final List<CategoryResponse> findCategories = getCategoriesByQueryParam(queryParam);
		final List<SubcategoryResponse> findSubcategories = getSubcategoriesByQueryParam(queryParam);
		List<ProductSimpleResponse> findProducts = getProductsByQueryParam(queryParam);

		return SearchResponse.of(findCountries, findCities, findCategories, findSubcategories, findProducts);
	}

	private List<CountrySimpleResponse> getCountriesByQueryParam(final String queryParam) {
		final List<CountryType> countryTypes = CountryType.getCountryTypesByPartialString(queryParam);

		return countryService.getAllCountriesByCountryTypes(countryTypes);
	}

	private List<CityResponse> getCitiesByQueryParam(final String queryParam) {
		final List<CityType> cityTypes = CityType.getCityTypesByPartialString(queryParam);

		return cityService.getAllCitiesByCityTypes(cityTypes);
	}

	private List<CategoryResponse> getCategoriesByQueryParam(final String queryParam) {
		final List<CategoryName> categoryNames = CategoryName.findByPartialString(queryParam);

		return categoryService.findCategoriesByCategoryNames(categoryNames);
	}

	private List<SubcategoryResponse> getSubcategoriesByQueryParam(final String queryParam) {
		final List<SubcategoryName> subcategoryNames = SubcategoryName.findByPartialString(queryParam);

		return subcategoryService.findSubcategoriesBySubcategoryNames(subcategoryNames);
	}

	private List<ProductSimpleResponse> getProductsByQueryParam(final String queryParam) {
		return productService.getProductsByPartialName(queryParam);
	}

}
