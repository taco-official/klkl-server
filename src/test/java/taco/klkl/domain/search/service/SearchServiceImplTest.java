package taco.klkl.domain.search.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import taco.klkl.domain.category.domain.Category;
import taco.klkl.domain.category.domain.CategoryName;
import taco.klkl.domain.category.domain.Subcategory;
import taco.klkl.domain.category.domain.SubcategoryName;
import taco.klkl.domain.category.dto.response.CategoryResponseDto;
import taco.klkl.domain.category.dto.response.SubcategoryResponseDto;
import taco.klkl.domain.category.service.CategoryService;
import taco.klkl.domain.category.service.SubcategoryService;
import taco.klkl.domain.region.domain.City;
import taco.klkl.domain.region.domain.Country;
import taco.klkl.domain.region.domain.Currency;
import taco.klkl.domain.region.domain.Region;
import taco.klkl.domain.region.dto.response.CityResponseDto;
import taco.klkl.domain.region.dto.response.CountrySimpleResponseDto;
import taco.klkl.domain.region.enums.CityType;
import taco.klkl.domain.region.enums.CountryType;
import taco.klkl.domain.region.service.CityService;
import taco.klkl.domain.region.service.CountryService;
import taco.klkl.domain.search.dto.response.SearchResponseDto;

@ExtendWith(MockitoExtension.class)
class SearchServiceImplTest {

	@InjectMocks
	SearchServiceImpl searchService;

	@Mock
	CountryService countryService;

	@Mock
	CityService cityService;

	@Mock
	CategoryService categoryService;

	@Mock
	SubcategoryService subcategoryService;

	@Mock
	Region region;

	@Mock
	Currency currency;

	private final Country country = Country.of(CountryType.MALAYSIA, region, "flag", "photo", currency);
	private final City city = City.of(country, CityType.BORACAY);
	private final Category category = Category.of(CategoryName.CLOTHES);
	private final Subcategory subcategory = Subcategory.of(category, SubcategoryName.MAKEUP);

	@Test
	@DisplayName("SearchService 테스트")
	void testGetSearchResult() {
		// given
		String queryParam = "test";

		List<CountrySimpleResponseDto> mockCountries = Collections.singletonList(
			CountrySimpleResponseDto.from(country));
		List<CityResponseDto> mockCities = Collections.singletonList(CityResponseDto.from(city));
		List<CategoryResponseDto> mockCategories = Collections.singletonList(CategoryResponseDto.from(category));
		List<SubcategoryResponseDto> mockSubcategories = Collections.singletonList(
			SubcategoryResponseDto.from(subcategory));

		when(countryService.getAllCountriesByCountryTypes(any(List.class))).thenReturn(mockCountries);
		when(cityService.getAllCitiesByCityTypes(any(List.class))).thenReturn(mockCities);
		when(categoryService.getCategoriesByCategoryNames(any(List.class))).thenReturn(mockCategories);
		when(subcategoryService.getSubcategoriesBySubcategoryNames(any(List.class))).thenReturn(mockSubcategories);

		// when
		SearchResponseDto result = searchService.getSearchResult(queryParam);

		// then
		assertThat(result).isNotNull();
		assertThat(result.countries()).isEqualTo(mockCountries);
		assertThat(result.cities()).isEqualTo(mockCities);
		assertThat(result.categories()).isEqualTo(mockCategories);
		assertThat(result.subcategories()).isEqualTo(mockSubcategories);
	}
}
