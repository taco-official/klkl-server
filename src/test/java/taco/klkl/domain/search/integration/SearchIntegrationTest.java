package taco.klkl.domain.search.integration;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import jakarta.transaction.Transactional;
import taco.klkl.domain.search.dto.response.SearchResponse;
import taco.klkl.domain.search.service.SearchService;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class SearchIntegrationTest {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	SearchService searchService;

	@Test
	void getSearchTest() throws Exception {
		// given
		String query = "리";
		SearchResponse searchResponse = searchService.getSearchResult(query);

		// when & then
		mockMvc.perform(get("/v1/search")
				.param("q", query))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code", is("C000")))
			.andExpect(jsonPath("$.isSuccess", is(true)))
			.andExpect(jsonPath("$.data.countries", hasSize(searchResponse.countries().size())))
			.andExpect(jsonPath("$.data.cities", hasSize(searchResponse.cities().size())))
			.andExpect(jsonPath("$.data.categories", hasSize(searchResponse.categories().size())))
			.andExpect(jsonPath("$.data.subcategories", hasSize(searchResponse.subcategories().size())))
			.andExpect(jsonPath("$.data.products", hasSize(searchResponse.products().size())));
	}
}
