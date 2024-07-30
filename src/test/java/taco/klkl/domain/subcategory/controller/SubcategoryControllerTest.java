package taco.klkl.domain.subcategory.controller;

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

import taco.klkl.domain.subcategory.dto.response.SubcategoryResponseDto;
import taco.klkl.domain.subcategory.exception.CategoryNotFoundException;
import taco.klkl.domain.subcategory.sevice.SubcategoryService;

@WebMvcTest(SubcategoryController.class)
public class SubcategoryControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private SubcategoryService subcategoryService;

	@Test
	@DisplayName("valid id가 들어올 경우 GlobalResponse로 Wrapping되어 나오는지 Test")
	public void testGetSubcategoryWithValidId() throws Exception {
		// given
		List<SubcategoryResponseDto> subcategoryResponseDto = Arrays.asList(
			new SubcategoryResponseDto(1L, "즉석식품"),
			new SubcategoryResponseDto(2L, "스낵")
		);

		// when
		when(subcategoryService.getSubcategories(anyLong())).thenReturn(subcategoryResponseDto);

		// then
		mockMvc.perform(get("/v1/categories/1/subcategories")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.isSuccess", is(true)))
			.andExpect(jsonPath("$.code", is("C000")))
			.andExpect(jsonPath("$.data", hasSize(2)))
			.andExpect(jsonPath("$.data[0].id", is(1)))
			.andExpect(jsonPath("$.data[0].name", is("즉석식품")))
			.andExpect(jsonPath("$.data[1].id", is(2)))
			.andExpect(jsonPath("$.data[1].name", is("스낵")))
			.andExpect(jsonPath("$.timestamp", notNullValue()));

		verify(subcategoryService, times(1)).getSubcategories(anyLong());
	}

	@Test
	@DisplayName("invalid한 id가 들어올 경우 GlobalException으로 Wrapping되어 나오는지 Test")
	public void testGetSubcategoryWithInvalidId() throws Exception {
		// given
		when(subcategoryService.getSubcategories(anyLong())).thenThrow(new CategoryNotFoundException());

		// when & then
		mockMvc.perform(get("/v1/categories/999/subcategories")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.isSuccess", is(false)))
			.andExpect(jsonPath("$.code", is("C060")))
			.andExpect(jsonPath("$.data.message", is("존재하지 않는 카테고리 ID 입니다.")))
			.andExpect(jsonPath("$.timestamp", notNullValue()));

		verify(subcategoryService, times(1)).getSubcategories(anyLong());
	}
}
