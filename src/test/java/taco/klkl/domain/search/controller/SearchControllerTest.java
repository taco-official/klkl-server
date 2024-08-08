package taco.klkl.domain.search.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import taco.klkl.domain.category.domain.Category;
import taco.klkl.domain.category.domain.CategoryName;
import taco.klkl.domain.category.domain.Subcategory;
import taco.klkl.domain.category.domain.SubcategoryName;
import taco.klkl.domain.category.dto.response.CategoryResponseDto;
import taco.klkl.domain.category.dto.response.SubcategoryResponseDto;
import taco.klkl.domain.region.domain.City;
import taco.klkl.domain.region.domain.Country;
import taco.klkl.domain.region.domain.Currency;
import taco.klkl.domain.region.domain.Region;
import taco.klkl.domain.region.dto.response.CityResponseDto;
import taco.klkl.domain.region.dto.response.CountrySimpleResponseDto;
import taco.klkl.domain.region.enums.CityType;
import taco.klkl.domain.region.enums.CountryType;
import taco.klkl.domain.search.dto.response.SearchResponseDto;
import taco.klkl.domain.search.service.SearchService;

@WebMvcTest(SearchController.class)
public class SearchControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private SearchService searchService;

	private SearchResponseDto mockResponse;

	@Mock
	Region region;

	@Mock
	Currency currency;

	private final Country country = Country.of(CountryType.MALAYSIA, region, "flag", "photo", currency);
	private final City city = City.of(country, CityType.BORACAY);
	private final Category category = Category.of(CategoryName.CLOTHES);
	private final Subcategory subcategory = Subcategory.of(category, SubcategoryName.MAKEUP);

	@BeforeEach
	void setUp() {
		// Mock 데이터 생성
		mockResponse = SearchResponseDto.of(
			Collections.singletonList(CountrySimpleResponseDto.from(country)),
			Collections.singletonList(CityResponseDto.from(city)),
			Collections.singletonList(CategoryResponseDto.from(category)),
			Collections.singletonList(SubcategoryResponseDto.from(subcategory))
		);
	}

	@Test
	void testGetSearchByQuery() throws Exception {
		// given
		String query = "test";

		when(searchService.getSearchResult(query)).thenReturn(mockResponse);

		// when & then
		mockMvc.perform(get("/v1/search")
				.param("q", query))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.countries").isArray())
			.andExpect(jsonPath("$.data.cities").isArray())
			.andExpect(jsonPath("$.data.categories").isArray())
			.andExpect(jsonPath("$.data.subcategories").isArray());
	}
}
