package taco.klkl.domain.category.controller;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import taco.klkl.domain.category.domain.Category;
import taco.klkl.domain.category.domain.CategoryName;
import taco.klkl.domain.category.domain.Filter;
import taco.klkl.domain.category.domain.FilterName;
import taco.klkl.domain.category.domain.Subcategory;
import taco.klkl.domain.category.domain.SubcategoryFilter;
import taco.klkl.domain.category.domain.SubcategoryName;
import taco.klkl.domain.category.dto.response.SubcategoryWithFilterResponseDto;
import taco.klkl.domain.category.exception.SubcategoryNotFoundException;
import taco.klkl.domain.category.service.SubcategoryFilterService;
import taco.klkl.domain.category.service.SubcategoryService;
import taco.klkl.global.error.exception.ErrorCode;

@WebMvcTest(FilterController.class)
public class FilterControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private SubcategoryService subcategoryService;

	@MockBean
	private SubcategoryFilterService subcategoryFilterService;

	@Test
	@DisplayName("존재하는 Subcategory Id 쿼리가 들어왔을 경우, Subcategory와 Filter가 잘 나오는지 테스트")
	public void testGetFilterWithValidQuery() throws Exception {
		// given
		List<Long> subcategoryIds = Arrays.asList(1L, 2L);

		Category mockCategory = Category.of(CategoryName.FOOD);
		Subcategory mockSubcategory1 = mock(Subcategory.class);
		Subcategory mockSubcategory2 = mock(Subcategory.class);
		Filter mockFilter1 = mock(Filter.class);
		Filter mockFilter2 = mock(Filter.class);

		SubcategoryFilter subcategoryFilter1 = SubcategoryFilter.of(mockSubcategory1, mockFilter1);
		SubcategoryFilter subcategoryFilter2 = SubcategoryFilter.of(mockSubcategory1, mockFilter2);
		SubcategoryFilter subcategoryFilter3 = SubcategoryFilter.of(mockSubcategory2, mockFilter1);

		List<Subcategory> mockSubcategoryList = Arrays.asList(mockSubcategory1, mockSubcategory2);

		List<SubcategoryFilter> mockSubcategory1FilterList = Arrays.asList(subcategoryFilter1, subcategoryFilter2);
		List<SubcategoryFilter> mockSubcategory2FilterList = Arrays.asList(subcategoryFilter3);

		when(mockSubcategory1.getId()).thenReturn(1L);
		when(mockSubcategory1.getName()).thenReturn(SubcategoryName.INSTANT_FOOD);
		when(mockSubcategory1.getSubcategoryFilters()).thenReturn(mockSubcategory1FilterList);
		when(mockSubcategory2.getId()).thenReturn(2L);
		when(mockSubcategory2.getName()).thenReturn(SubcategoryName.SNACK);
		when(mockSubcategory2.getSubcategoryFilters()).thenReturn(mockSubcategory2FilterList);

		when(mockFilter1.getId()).thenReturn(1L);
		when(mockFilter1.getName()).thenReturn(FilterName.CONVENIENCE_STORE);
		when(mockFilter2.getId()).thenReturn(2L);
		when(mockFilter2.getName()).thenReturn(FilterName.CILANTRO);

		List<SubcategoryWithFilterResponseDto> mockResponse = mockSubcategoryList.stream()
			.map(SubcategoryWithFilterResponseDto::from)
			.toList();

		when(subcategoryService.getSubcategoryList(subcategoryIds)).thenReturn(mockSubcategoryList);
		when(subcategoryFilterService.getFilters(anyList())).thenReturn(mockResponse);

		// when & then
		mockMvc.perform(get("/v1/filters")
				.param("subcategories", "1,2")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.isSuccess", is(true)))
			.andExpect(jsonPath("$.code", is("C000")))
			.andExpect(jsonPath("$.data[0].subcategoryId", is(1)))
			.andExpect(jsonPath("$.data[0].subcategory", is(SubcategoryName.INSTANT_FOOD.getKoreanName())))
			.andExpect(jsonPath("$.data[0].filters[0].filterId", is(1)))
			.andExpect(jsonPath("$.data[0].filters[0].filter", is(FilterName.CONVENIENCE_STORE.getKoreanName())))
			.andExpect(jsonPath("$.data[0].filters[1].filterId", is(2)))
			.andExpect(jsonPath("$.data[0].filters[1].filter", is(FilterName.CILANTRO.getKoreanName())))
			.andExpect(jsonPath("$.data[1].subcategoryId", is(2)))
			.andExpect(jsonPath("$.data[1].subcategory", is(SubcategoryName.SNACK.getKoreanName())))
			.andExpect(jsonPath("$.data[1].filters[0].filterId", is(1)))
			.andExpect(jsonPath("$.data[1].filters[0].filter", is(FilterName.CONVENIENCE_STORE.getKoreanName())))
			.andExpect(jsonPath("$.timestamp", notNullValue()));

		verify(subcategoryService, times(1)).getSubcategoryList(subcategoryIds);
	}

	@Test
	@DisplayName("존재하는 Subcategory Id 쿼리가 들어왔지만 Filter가 없는 Subcategory일 경우, Subcategory와 Filter가 잘 나오는지 테스트")
	public void testGetFilterWithValidQueryButEmptyOne() throws Exception {
		// given
		List<Long> subcategoryIds = Arrays.asList(1L, 2L);

		Category mockCategory = Category.of(CategoryName.FOOD);
		Subcategory mockSubcategory1 = mock(Subcategory.class);
		Subcategory mockSubcategory2 = mock(Subcategory.class);
		Filter mockFilter1 = mock(Filter.class);
		Filter mockFilter2 = mock(Filter.class);

		SubcategoryFilter subcategoryFilter1 = SubcategoryFilter.of(mockSubcategory1, mockFilter1);
		SubcategoryFilter subcategoryFilter2 = SubcategoryFilter.of(mockSubcategory1, mockFilter2);

		List<Subcategory> mockSubcategoryList = Arrays.asList(mockSubcategory1, mockSubcategory2);

		List<SubcategoryFilter> mockSubcategory1FilterList = Arrays.asList(subcategoryFilter1, subcategoryFilter2);

		when(mockSubcategory1.getId()).thenReturn(1L);
		when(mockSubcategory1.getName()).thenReturn(SubcategoryName.INSTANT_FOOD);
		when(mockSubcategory1.getSubcategoryFilters()).thenReturn(mockSubcategory1FilterList);
		when(mockSubcategory2.getId()).thenReturn(2L);
		when(mockSubcategory2.getName()).thenReturn(SubcategoryName.SNACK);
		when(mockSubcategory2.getSubcategoryFilters()).thenReturn(Collections.emptyList());

		when(mockFilter1.getId()).thenReturn(1L);
		when(mockFilter1.getName()).thenReturn(FilterName.CONVENIENCE_STORE);
		when(mockFilter2.getId()).thenReturn(2L);
		when(mockFilter2.getName()).thenReturn(FilterName.CILANTRO);

		List<SubcategoryWithFilterResponseDto> mockResponse = mockSubcategoryList.stream()
			.map(SubcategoryWithFilterResponseDto::from)
			.toList();

		when(subcategoryService.getSubcategoryList(subcategoryIds)).thenReturn(mockSubcategoryList);
		when(subcategoryFilterService.getFilters(anyList())).thenReturn(mockResponse);

		// when & then
		mockMvc.perform(get("/v1/filters")
				.param("subcategories", "1,2")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.isSuccess", is(true)))
			.andExpect(jsonPath("$.code", is("C000")))
			.andExpect(jsonPath("$.data[0].subcategoryId", is(1)))
			.andExpect(jsonPath("$.data[0].subcategory", is(SubcategoryName.INSTANT_FOOD.getKoreanName())))
			.andExpect(jsonPath("$.data[0].filters[0].filterId", is(1)))
			.andExpect(jsonPath("$.data[0].filters[0].filter", is(FilterName.CONVENIENCE_STORE.getKoreanName())))
			.andExpect(jsonPath("$.data[0].filters[1].filterId", is(2)))
			.andExpect(jsonPath("$.data[0].filters[1].filter", is(FilterName.CILANTRO.getKoreanName())))
			.andExpect(jsonPath("$.data[1].subcategoryId", is(2)))
			.andExpect(jsonPath("$.data[1].subcategory", is(SubcategoryName.SNACK.getKoreanName())))
			.andExpect(jsonPath("$.data[1].filters.size()", is(0)))
			.andExpect(jsonPath("$.timestamp", notNullValue()));

		verify(subcategoryService, times(1)).getSubcategoryList(subcategoryIds);
	}

	@Test
	@DisplayName("존재하지 않는 Subcategory Id 쿼리가 들어올 경우, SubcategoryNotFound Error Response를 반환하는지 테스트")
	public void testGetFilterWithValidQueryButNotExist() throws Exception {
		//given
		when(subcategoryService.getSubcategoryList(anyList())).thenThrow(new SubcategoryNotFoundException());

		//when & then
		mockMvc.perform(get("/v1/filters")
				.param("subcategories", "1,2")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.isSuccess", is(false)))
			.andExpect(jsonPath("$.code", is(ErrorCode.SUBCATEGORY_ID_NOT_FOUND.getCode())))
			.andExpect(jsonPath("$.data.message", is(ErrorCode.SUBCATEGORY_ID_NOT_FOUND.getMessage())))
			.andExpect(jsonPath("$.timestamp", notNullValue()));
	}

	@Test
	@DisplayName("올바르지 않은 쿼리가 들어올 경우, INVALID_QUERY_FORMAT을 반환하는지 테스트")
	public void testGetFilterWithInvalidQueryFormat() throws Exception {
		//given
		when(subcategoryService.getSubcategoryList(anyList())).thenThrow(MethodArgumentTypeMismatchException.class);

		//when & then
		mockMvc.perform(get("/v1/filters")
				.param("subcategories", "1,2")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.isSuccess", is(false)))
			.andExpect(jsonPath("$.code", is(ErrorCode.QUERY_TYPE_MISMATCH.getCode())))
			.andExpect(jsonPath("$.data.message", is(ErrorCode.QUERY_TYPE_MISMATCH.getMessage())))
			.andExpect(jsonPath("$.timestamp", notNullValue()));
	}
}
