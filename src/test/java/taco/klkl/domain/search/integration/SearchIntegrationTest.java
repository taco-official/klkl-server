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
import taco.klkl.domain.search.dto.response.SearchResponseDto;
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
		String query = "이";
		SearchResponseDto searchResponseDto = searchService.getSearchResult(query);

		// when & then
		mockMvc.perform(get("/v1/search")
				.param("q", query))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code", is("C000")))
			.andExpect(jsonPath("$.isSuccess", is(true)))
			.andExpect(jsonPath("$.data.countries", hasSize(searchResponseDto.countries().size())))
			.andExpect(jsonPath("$.data.cities", hasSize(searchResponseDto.cities().size())))
			.andExpect(jsonPath("$.data.categories", hasSize(searchResponseDto.categories().size())))
			.andExpect(jsonPath("$.data.subcategories", hasSize(searchResponseDto.subcategories().size())));
	}
}
