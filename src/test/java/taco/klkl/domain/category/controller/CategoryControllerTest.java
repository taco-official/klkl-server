package taco.klkl.domain.category.controller;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import taco.klkl.domain.category.dto.response.CategoryResponseDto;
import taco.klkl.domain.category.service.CategoryService;

@WebMvcTest(CategoryController.class)
public class CategoryControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private CategoryService categoryService;

	@Test
	@DisplayName("카테고리 컨트롤러 GlobalResponse로 Wrapping되어 나오는지 Test")
	public void testGetCategory() throws Exception {
		// given
		List<CategoryResponseDto> categoryResponsDtos = Arrays.asList(
			new CategoryResponseDto(1L, "Category1"),
			new CategoryResponseDto(2L, "Category2")
		);

		// when
		when(categoryService.getCategories()).thenReturn(categoryResponsDtos);

		// then
		mockMvc.perform(get("/v1/categories")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.isSuccess", is(true)))
			.andExpect(jsonPath("$.code", is("C000")))
			.andExpect(jsonPath("$.data", hasSize(2)))
			.andExpect(jsonPath("$.data[0].id", is(1)))
			.andExpect(jsonPath("$.data[0].name", is("Category1")))
			.andExpect(jsonPath("$.data[1].id", is(2)))
			.andExpect(jsonPath("$.data[1].name", is("Category2")))
			.andExpect(jsonPath("$.timestamp", notNullValue()));

		verify(categoryService, times(1)).getCategories();
	}
}
