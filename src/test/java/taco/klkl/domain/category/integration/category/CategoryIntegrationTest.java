package taco.klkl.domain.category.integration.category;

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
import taco.klkl.domain.category.dto.response.category.CategoryResponse;
import taco.klkl.domain.category.service.category.CategoryService;
import taco.klkl.global.error.exception.ErrorCode;

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
		List<CategoryResponse> categoryResponse = categoryService.findAllCategories();

		//then
		mockMvc.perform(get("/v1/categories/hierarchy")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data", hasSize(categoryResponse.size())))
			.andExpect(jsonPath("$.isSuccess", is(true)))
			.andExpect(jsonPath("$.data[0].subcategories",
				hasSize(categoryResponse.get(0).subcategories().size())))
			.andExpect(jsonPath("$.data[1].subcategories",
				hasSize(categoryResponse.get(1).subcategories().size())))
			.andExpect(jsonPath("$.data[2].subcategories",
				hasSize(categoryResponse.get(2).subcategories().size())))
			.andExpect(jsonPath("$.data[3].subcategories",
				hasSize(categoryResponse.get(3).subcategories().size())));
	}
}
