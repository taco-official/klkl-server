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
import taco.klkl.domain.category.dto.response.CategoryResponse;
import taco.klkl.domain.category.dto.response.SubcategoryResponse;
import taco.klkl.domain.product.domain.Product;
import taco.klkl.domain.product.domain.Rating;
import taco.klkl.domain.product.dto.response.ProductSimpleResponse;
import taco.klkl.domain.region.domain.City;
import taco.klkl.domain.region.domain.Country;
import taco.klkl.domain.region.domain.Currency;
import taco.klkl.domain.region.domain.Region;
import taco.klkl.domain.region.dto.response.CityResponse;
import taco.klkl.domain.region.dto.response.CountrySimpleResponse;
import taco.klkl.domain.region.enums.CityType;
import taco.klkl.domain.region.enums.CountryType;
import taco.klkl.domain.search.dto.response.SearchResponse;
import taco.klkl.domain.search.service.SearchService;
import taco.klkl.domain.user.domain.User;
import taco.klkl.global.common.constants.UserConstants;

@WebMvcTest(SearchController.class)
public class SearchControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private SearchService searchService;

	private SearchResponse mockResponse;

	@Mock
	Region region;

	@Mock
	Currency currency;

	private final User user = UserConstants.TEST_USER;
	private final Country country = Country.of(CountryType.MALAYSIA, region, "flag", "photo", currency);
	private final City city = City.of(country, CityType.BORACAY);
	private final Category category = Category.of(CategoryName.CLOTHES);
	private final Subcategory subcategory = Subcategory.of(category, SubcategoryName.MAKEUP);
	private final Product product = Product.of(
		"name",
		"description",
		"address",
		1000,
		Rating.FIVE,
		user,
		city,
		subcategory,
		currency
	);

	@BeforeEach
	void setUp() {
		// Mock 데이터 생성
		mockResponse = SearchResponse.of(
			Collections.singletonList(CountrySimpleResponse.from(country)),
			Collections.singletonList(CityResponse.from(city)),
			Collections.singletonList(CategoryResponse.from(category)),
			Collections.singletonList(SubcategoryResponse.from(subcategory)),
			Collections.singletonList(ProductSimpleResponse.from(product))
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
			.andExpect(jsonPath("$.data.subcategories").isArray())
			.andExpect(jsonPath("$.data.products").isArray());
	}
}
