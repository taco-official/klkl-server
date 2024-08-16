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
import taco.klkl.domain.category.dto.response.CategoryResponse;
import taco.klkl.domain.category.dto.response.CategoryWithSubcategoryResponse;
import taco.klkl.domain.category.service.CategoryService;
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
		List<CategoryResponse> categoryResponse = categoryService.getCategories();

		//then
		mockMvc.perform(get("/v1/categories")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data", hasSize(categoryResponse.size())))
			.andExpect(jsonPath("$.isSuccess", is(true)))
			.andExpect(jsonPath("$.code", is("C000")));
	}

	@Test
	@DisplayName("valid한 id값이 들어왔을 때 반환값이 제대로 전달되는지 테스트")
	public void testGetCategoriesWithValidId() throws Exception {
		//given
		CategoryWithSubcategoryResponse categoryWithSubcategoryResponse = categoryService.getSubcategories(300L);

		//when, then
		mockMvc.perform(get("/v1/categories/300/subcategories")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.id", is(categoryWithSubcategoryResponse.id().intValue())))
			.andExpect(jsonPath("$.data.name", is(categoryWithSubcategoryResponse.name())))
			.andExpect(jsonPath("$.data.subcategories[0].id",
				is(categoryWithSubcategoryResponse.subcategories().get(0).id().intValue())))
			.andExpect(jsonPath("$.data.subcategories[0].name",
				is(categoryWithSubcategoryResponse.subcategories().get(0).name())))
			.andExpect(jsonPath("$.data.subcategories[1].id",
				is(categoryWithSubcategoryResponse.subcategories().get(1).id().intValue())))
			.andExpect(jsonPath("$.data.subcategories[1].name",
				is(categoryWithSubcategoryResponse.subcategories().get(1).name())))
			.andExpect(jsonPath("$.isSuccess", is(true)))
			.andExpect(jsonPath("$.code", is("C000")));
	}

	@Test
	@DisplayName("invalid한 id값이 들어왔을 때 오류가 전달되는지 테스트")
	public void testGetCategoriesWithInvalidId() throws Exception {
		//given

		//when, then
		mockMvc.perform(get("/v1/categories/999/subcategories")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.isSuccess", is(false)))
			.andExpect(jsonPath("$.code", is(ErrorCode.CATEGORY_ID_NOT_FOUND.getCode())))
			.andExpect(jsonPath("$.data.message", is(ErrorCode.CATEGORY_ID_NOT_FOUND.getMessage())));
	}
}
