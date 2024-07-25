package taco.klkl.domain.category.integration;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import jakarta.transaction.Transactional;
import taco.klkl.domain.category.dto.response.CategoryResponseDto;
import taco.klkl.domain.category.service.CategoryService;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class CategoryIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private CategoryService categoryService;

	@Test
	@DisplayName("카테고리 목록 반환 API 통합 Test")
	void testGetAllCategories() throws Exception {
		// given
		List<CategoryResponseDto> categoryResponsDtos = categoryService.getCategories();

		//then
		mockMvc.perform(get("/v1/categories")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data", hasSize(categoryResponsDtos.size())))
			.andExpect(jsonPath("$.isSuccess", is(true)))
			.andExpect(jsonPath("$.code", is("C000")));
	}
}
