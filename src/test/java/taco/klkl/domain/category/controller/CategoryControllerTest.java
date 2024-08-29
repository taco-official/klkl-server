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

import taco.klkl.domain.category.controller.category.CategoryController;
import taco.klkl.domain.category.dao.category.CategoryRepository;
import taco.klkl.domain.category.domain.category.Category;
import taco.klkl.domain.category.domain.category.CategoryType;
import taco.klkl.domain.category.domain.subcategory.Subcategory;
import taco.klkl.domain.category.domain.subcategory.SubcategoryType;
import taco.klkl.domain.category.dto.response.category.CategoryResponse;
import taco.klkl.domain.category.dto.response.subcategory.SubcategoryResponse;
import taco.klkl.domain.category.exception.category.CategoryNotFoundException;
import taco.klkl.domain.category.service.category.CategoryService;
import taco.klkl.global.error.exception.ErrorCode;

@WebMvcTest(CategoryController.class)
public class CategoryControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private CategoryService categoryService;

	private final Category category = Category.of(CategoryType.FOOD);
	private final Subcategory subcategory1 = Subcategory.of(category, SubcategoryType.DRESS);
	private final Subcategory subcategory2 = Subcategory.of(category, SubcategoryType.HAIR_CARE);
	private final List<Subcategory> subcategories = Arrays.asList(subcategory1, subcategory2);
	private final List<Subcategory> subcategories2 = Arrays.asList(subcategory1, subcategory2);

	@Test
	@DisplayName("카테고리 컨트롤러 GlobalResponse로 Wrapping되어 나오는지 Test")
	public void testFindAllCategories() throws Exception {
		// given
		List<CategoryResponse> categoryResponse = Arrays.asList(
			new CategoryResponse(1L, "Category1", subcategories.stream()
				.map(SubcategoryResponse::from)
				.toList()),
			new CategoryResponse(2L, "Category2", subcategories2.stream()
				.map(SubcategoryResponse::from)
				.toList())
		);

		// when
		when(categoryService.findAllCategories()).thenReturn(categoryResponse);

		// then
		mockMvc.perform(get("/v1/categories")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.isSuccess", is(true)))
			.andExpect(jsonPath("$.data", hasSize(2)))
			.andExpect(jsonPath("$.data[0].id", is(1)))
			.andExpect(jsonPath("$.data[0].name", is("Category1")))
			.andExpect(jsonPath("$.data[1].id", is(2)))
			.andExpect(jsonPath("$.data[1].name", is("Category2")))
			.andExpect(jsonPath("$.timestamp", notNullValue()));

		verify(categoryService, times(1)).findAllCategories();
	}

	@Test
	@DisplayName("valid id가 들어올 경우 Categoy와 Subcategory가 잘 나오는지 Test")
	public void testFindSubcategoryWithValidIdById() throws Exception {
		// given
		Long id = 1L;
		Category mockCategory = mock(Category.class);
		CategoryRepository mockCategoryRepository = mock(CategoryRepository.class);
		when(mockCategory.getId()).thenReturn(id);
		when(mockCategory.getName()).thenReturn(CategoryType.FOOD.getName());

		when(mockCategoryRepository.findById(id)).thenReturn(Optional.of(mockCategory));
		when(mockCategory.getSubcategories()).thenReturn(subcategories);
		CategoryResponse response = CategoryResponse.from(mockCategory);

		// when
		when(categoryService.findSubCategoriesByCategoryId(id)).thenReturn(response);

		// then
		mockMvc.perform(get("/v1/categories/1/subcategories")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.isSuccess", is(true)))
			.andExpect(jsonPath("$.data.id", is(1)))
			.andExpect(jsonPath("$.data.name", is(CategoryType.FOOD.getName())))
			.andExpect(jsonPath("$.data.subcategories[0].id", is(subcategory1.getId())))
			.andExpect(jsonPath("$.data.subcategories[0].name", is(SubcategoryType.DRESS.getName())))
			.andExpect(jsonPath("$.data.subcategories[1].id", is(subcategory2.getId())))
			.andExpect(jsonPath("$.data.subcategories[1].name", is(SubcategoryType.HAIR_CARE.getName())))
			.andExpect(jsonPath("$.timestamp", notNullValue()));

		verify(categoryService, times(1)).findSubCategoriesByCategoryId(id);
	}

	@Test
	@DisplayName("invalid한 id가 들어올 경우 GlobalException으로 Wrapping되어 나오는지 Test")
	public void testFindSubcategoryWithInvalidIdById() throws Exception {
		// given
		when(categoryService.findSubCategoriesByCategoryId(anyLong())).thenThrow(new CategoryNotFoundException());

		// when & then
		mockMvc.perform(get("/v1/categories/999/subcategories")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.isSuccess", is(false)))
			.andExpect(jsonPath("$.status", is(ErrorCode.CATEGORY_NOT_FOUND.getHttpStatus().value())))
			.andExpect(jsonPath("$.data.message", is(ErrorCode.CATEGORY_NOT_FOUND.getMessage())));

		verify(categoryService, times(1)).findSubCategoriesByCategoryId(anyLong());
	}
}
