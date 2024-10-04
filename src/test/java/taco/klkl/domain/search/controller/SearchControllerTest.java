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
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import taco.klkl.domain.category.domain.category.Category;
import taco.klkl.domain.category.domain.category.CategoryType;
import taco.klkl.domain.category.domain.subcategory.Subcategory;
import taco.klkl.domain.category.domain.subcategory.SubcategoryType;
import taco.klkl.domain.category.dto.response.category.CategorySimpleResponse;
import taco.klkl.domain.category.dto.response.subcategory.SubcategorySimpleResponse;
import taco.klkl.domain.region.domain.city.City;
import taco.klkl.domain.region.domain.city.CityType;
import taco.klkl.domain.region.domain.country.Country;
import taco.klkl.domain.region.domain.country.CountryType;
import taco.klkl.domain.region.domain.currency.Currency;
import taco.klkl.domain.region.domain.region.Region;
import taco.klkl.domain.region.dto.response.city.CitySimpleResponse;
import taco.klkl.domain.region.dto.response.country.CountrySimpleResponse;
import taco.klkl.domain.search.dto.response.SearchResponse;
import taco.klkl.domain.search.service.SearchService;
import taco.klkl.domain.token.service.TokenProvider;
import taco.klkl.global.config.security.TestSecurityConfig;
import taco.klkl.global.util.ResponseUtil;
import taco.klkl.global.util.TokenUtil;

@WebMvcTest(SearchController.class)
@Import(TestSecurityConfig.class)
public class SearchControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private TokenProvider tokenProvider;

	@MockBean
	private ResponseUtil responseUtil;

	@MockBean
	private TokenUtil tokenUtil;

	@MockBean
	private SearchService searchService;

	@Mock
	Region region;

	@Mock
	Currency currency;

	private SearchResponse mockResponse;

	private final Country country = Country.of(CountryType.MALAYSIA, region, "wallpaper", currency);
	private final City city = City.of(CityType.BORACAY, country);
	private final Category category = Category.of(CategoryType.CLOTHES);
	private final Subcategory subcategory = Subcategory.of(category, SubcategoryType.MAKEUP);

	@BeforeEach
	void setUp() {
		// Mock 데이터 생성
		mockResponse = SearchResponse.of(
			Collections.singletonList(CountrySimpleResponse.from(country)),
			Collections.singletonList(CitySimpleResponse.from(city)),
			Collections.singletonList(CategorySimpleResponse.from(category)),
			Collections.singletonList(SubcategorySimpleResponse.from(subcategory))
		);
	}

	@Test
	void testFindSearchByQuery() throws Exception {
		// given
		String query = "test";

		when(searchService.findSearchResult(query)).thenReturn(mockResponse);

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
