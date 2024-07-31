package taco.klkl.domain.category.controller;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import taco.klkl.domain.category.dao.CategoryRepository;
import taco.klkl.domain.category.domain.Category;
import taco.klkl.domain.category.domain.CategoryName;
import taco.klkl.domain.category.domain.Subcategory;
import taco.klkl.domain.category.domain.SubcategoryName;
import taco.klkl.domain.category.dto.response.CategoryResponseDto;
import taco.klkl.domain.category.dto.response.CategoryWithSubcategoryDto;
import taco.klkl.domain.category.exception.CategoryNotFoundException;
import taco.klkl.domain.category.service.CategoryService;
import taco.klkl.global.error.exception.ErrorCode;

@WebMvcTest(CategoryController.class)
public class CategoryControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private CategoryService categoryService;

	private final Category category = Category.of(CategoryName.FOOD);
	private final Subcategory subcategory1 = Subcategory.of(category, SubcategoryName.DRESS);
	private final Subcategory subcategory2 = Subcategory.of(category, SubcategoryName.HAIR_CARE);
	private final List<Subcategory> subcategories = Arrays.asList(subcategory1, subcategory2);

	@Test
	@DisplayName("카테고리 컨트롤러 GlobalResponse로 Wrapping되어 나오는지 Test")
	public void testGetCategory() throws Exception {
		// given
		List<CategoryResponseDto> categoryResponseDto = Arrays.asList(
			new CategoryResponseDto(1L, "Category1"),
			new CategoryResponseDto(2L, "Category2")
		);

		// when
		when(categoryService.getCategories()).thenReturn(categoryResponseDto);

		// then
		mockMvc.perform(get("/v1/categories")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.isSuccess", is(true)))
			.andExpect(jsonPath("$.code", is("C000")))
			.andExpect(jsonPath("$.data", hasSize(2)))
			.andExpect(jsonPath("$.data[0].id", is(1)))
			.andExpect(jsonPath("$.data[0].category", is("Category1")))
			.andExpect(jsonPath("$.data[1].id", is(2)))
			.andExpect(jsonPath("$.data[1].category", is("Category2")))
			.andExpect(jsonPath("$.timestamp", notNullValue()));

		verify(categoryService, times(1)).getCategories();
	}

	@Test
	@DisplayName("valid id가 들어올 경우 Categoy와 Subcategory가 잘 나오는지 Test")
	public void testGetSubcategoryWithValidId() throws Exception {
		// given
		Long categoryId = 1L;
		Category mockCategory = mock(Category.class);
		CategoryRepository mockCategoryRepository = mock(CategoryRepository.class);
		when(mockCategory.getId()).thenReturn(categoryId);
		when(mockCategory.getName()).thenReturn(CategoryName.FOOD);

		when(mockCategoryRepository.findById(categoryId)).thenReturn(Optional.of(mockCategory));
		when(mockCategory.getSubcategories()).thenReturn(subcategories);
		CategoryWithSubcategoryDto response = CategoryWithSubcategoryDto.from(mockCategory);

		// when
		when(categoryService.getSubcategories(categoryId)).thenReturn(response);

		// then
		mockMvc.perform(get("/v1/categories/1/subcategories")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.isSuccess", is(true)))
			.andExpect(jsonPath("$.code", is("C000")))
			.andExpect(jsonPath("$.data.categoryId", is(1)))
			.andExpect(jsonPath("$.data.category", is(CategoryName.FOOD.getName())))
			.andExpect(jsonPath("$.data.subcategories[0].id", is(subcategory1.getId())))
			.andExpect(jsonPath("$.data.subcategories[0].subcategory", is(SubcategoryName.DRESS.getName())))
			.andExpect(jsonPath("$.data.subcategories[1].id", is(subcategory2.getId())))
			.andExpect(jsonPath("$.data.subcategories[1].subcategory", is(SubcategoryName.HAIR_CARE.getName())))
			.andExpect(jsonPath("$.timestamp", notNullValue()));

		verify(categoryService, times(1)).getSubcategories(categoryId);
	}

	@Test
	@DisplayName("invalid한 id가 들어올 경우 GlobalException으로 Wrapping되어 나오는지 Test")
	public void testGetSubcategoryWithInvalidId() throws Exception {
		// given
		when(categoryService.getSubcategories(anyLong())).thenThrow(new CategoryNotFoundException());

		// when & then
		mockMvc.perform(get("/v1/categories/999/subcategories")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.isSuccess", is(false)))
			.andExpect(jsonPath("$.code", is(ErrorCode.CATEGORY_ID_NOT_FOUND.getCode())))
			.andExpect(jsonPath("$.data.message", is(ErrorCode.CATEGORY_ID_NOT_FOUND.getMessage())))
			.andExpect(jsonPath("$.timestamp", notNullValue()));

		verify(categoryService, times(1)).getSubcategories(anyLong());
	}
}
