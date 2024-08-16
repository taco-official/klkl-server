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
import taco.klkl.domain.category.domain.Subcategory;
import taco.klkl.domain.category.domain.SubcategoryName;
import taco.klkl.domain.category.domain.SubcategoryTag;
import taco.klkl.domain.category.domain.Tag;
import taco.klkl.domain.category.domain.TagName;
import taco.klkl.domain.category.dto.response.TagWithSubcategoryResponse;
import taco.klkl.domain.category.exception.SubcategoryNotFoundException;
import taco.klkl.domain.category.service.SubcategoryService;
import taco.klkl.domain.category.service.SubcategoryTagService;
import taco.klkl.global.error.exception.ErrorCode;

@WebMvcTest(TagController.class)
public class TagControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private SubcategoryService subcategoryService;

	@MockBean
	private SubcategoryTagService subcategoryTagService;

	@Test
	@DisplayName("존재하는 Subcategory Id 쿼리가 들어왔을 경우, Subcategory와 Filter가 잘 나오는지 테스트")
	public void testGetTagsByidsWithValidQuery() throws Exception {
		// given
		List<Long> ids = Arrays.asList(1L, 2L);

		Subcategory mockSubcategory1 = mock(Subcategory.class);
		Subcategory mockSubcategory2 = mock(Subcategory.class);
		Tag mockTag1 = mock(Tag.class);
		Tag mockTag2 = mock(Tag.class);

		SubcategoryTag subcategoryTag1 = SubcategoryTag.of(mockSubcategory1, mockTag1);
		SubcategoryTag subcategoryTag2 = SubcategoryTag.of(mockSubcategory1, mockTag2);
		SubcategoryTag subcategoryTag3 = SubcategoryTag.of(mockSubcategory2, mockTag1);

		List<Subcategory> mockSubcategoryList = Arrays.asList(mockSubcategory1, mockSubcategory2);

		List<SubcategoryTag> mockSubcategory1FilterList = Arrays.asList(subcategoryTag1, subcategoryTag2);
		List<SubcategoryTag> mockSubcategory2FilterList = Arrays.asList(subcategoryTag3);

		when(mockSubcategory1.getId()).thenReturn(1L);
		when(mockSubcategory1.getName()).thenReturn(SubcategoryName.INSTANT_FOOD);
		when(mockSubcategory1.getSubcategoryTags()).thenReturn(mockSubcategory1FilterList);
		when(mockSubcategory2.getId()).thenReturn(2L);
		when(mockSubcategory2.getName()).thenReturn(SubcategoryName.SNACK);
		when(mockSubcategory2.getSubcategoryTags()).thenReturn(mockSubcategory2FilterList);

		when(mockTag1.getId()).thenReturn(1L);
		when(mockTag1.getName()).thenReturn(TagName.CONVENIENCE_STORE);
		when(mockTag2.getId()).thenReturn(2L);
		when(mockTag2.getName()).thenReturn(TagName.CILANTRO);

		List<TagWithSubcategoryResponse> mockResponse = mockSubcategoryList.stream()
			.map(TagWithSubcategoryResponse::from)
			.toList();

		when(subcategoryService.getSubcategoryList(ids)).thenReturn(mockSubcategoryList);
		when(subcategoryTagService.getTagsBySubcategoryList(anyList())).thenReturn(mockResponse);

		// when & then
		mockMvc.perform(get("/v1/tags")
				.param("subcategories", "1,2")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.isSuccess", is(true)))
			.andExpect(jsonPath("$.code", is("C000")))
			.andExpect(jsonPath("$.data[0].id", is(1)))
			.andExpect(jsonPath("$.data[0].name", is(SubcategoryName.INSTANT_FOOD.getKoreanName())))
			.andExpect(jsonPath("$.data[0].tags[0].id", is(1)))
			.andExpect(jsonPath("$.data[0].tags[0].name", is(TagName.CONVENIENCE_STORE.getKoreanName())))
			.andExpect(jsonPath("$.data[0].tags[1].id", is(2)))
			.andExpect(jsonPath("$.data[0].tags[1].name", is(TagName.CILANTRO.getKoreanName())))
			.andExpect(jsonPath("$.data[1].id", is(2)))
			.andExpect(jsonPath("$.data[1].name", is(SubcategoryName.SNACK.getKoreanName())))
			.andExpect(jsonPath("$.data[1].tags[0].id", is(1)))
			.andExpect(jsonPath("$.data[1].tags[0].name", is(TagName.CONVENIENCE_STORE.getKoreanName())))
			.andExpect(jsonPath("$.timestamp", notNullValue()));

		verify(subcategoryService, times(1)).getSubcategoryList(ids);
	}

	@Test
	@DisplayName("존재하는 Subcategory Id 쿼리가 들어왔지만 Filter가 없는 Subcategory일 경우, Subcategory와 Filter가 잘 나오는지 테스트")
	public void testGetTagsByidsWithValidQueryButEmptyOne() throws Exception {
		// given
		List<Long> ids = Arrays.asList(1L, 2L);

		Category mockCategory = Category.of(CategoryName.FOOD);
		Subcategory mockSubcategory1 = mock(Subcategory.class);
		Subcategory mockSubcategory2 = mock(Subcategory.class);
		Tag mockTag1 = mock(Tag.class);
		Tag mockTag2 = mock(Tag.class);

		SubcategoryTag subcategoryTag1 = SubcategoryTag.of(mockSubcategory1, mockTag1);
		SubcategoryTag subcategoryTag2 = SubcategoryTag.of(mockSubcategory1, mockTag2);

		List<Subcategory> mockSubcategoryList = Arrays.asList(mockSubcategory1, mockSubcategory2);

		List<SubcategoryTag> mockSubcategory1FilterList = Arrays.asList(subcategoryTag1, subcategoryTag2);

		when(mockSubcategory1.getId()).thenReturn(1L);
		when(mockSubcategory1.getName()).thenReturn(SubcategoryName.INSTANT_FOOD);
		when(mockSubcategory1.getSubcategoryTags()).thenReturn(mockSubcategory1FilterList);
		when(mockSubcategory2.getId()).thenReturn(2L);
		when(mockSubcategory2.getName()).thenReturn(SubcategoryName.SNACK);
		when(mockSubcategory2.getSubcategoryTags()).thenReturn(Collections.emptyList());

		when(mockTag1.getId()).thenReturn(1L);
		when(mockTag1.getName()).thenReturn(TagName.CONVENIENCE_STORE);
		when(mockTag2.getId()).thenReturn(2L);
		when(mockTag2.getName()).thenReturn(TagName.CILANTRO);

		List<TagWithSubcategoryResponse> mockResponse = mockSubcategoryList.stream()
			.map(TagWithSubcategoryResponse::from)
			.toList();

		when(subcategoryService.getSubcategoryList(ids)).thenReturn(mockSubcategoryList);
		when(subcategoryTagService.getTagsBySubcategoryList(anyList())).thenReturn(mockResponse);

		// when & then
		mockMvc.perform(get("/v1/tags")
				.param("subcategories", "1,2")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.isSuccess", is(true)))
			.andExpect(jsonPath("$.code", is("C000")))
			.andExpect(jsonPath("$.data[0].id", is(1)))
			.andExpect(jsonPath("$.data[0].name", is(SubcategoryName.INSTANT_FOOD.getKoreanName())))
			.andExpect(jsonPath("$.data[0].tags[0].id", is(1)))
			.andExpect(jsonPath("$.data[0].tags[0].name", is(TagName.CONVENIENCE_STORE.getKoreanName())))
			.andExpect(jsonPath("$.data[0].tags[1].id", is(2)))
			.andExpect(jsonPath("$.data[0].tags[1].name", is(TagName.CILANTRO.getKoreanName())))
			.andExpect(jsonPath("$.data[1].id", is(2)))
			.andExpect(jsonPath("$.data[1].name", is(SubcategoryName.SNACK.getKoreanName())))
			.andExpect(jsonPath("$.data[1].tags.size()", is(0)))
			.andExpect(jsonPath("$.timestamp", notNullValue()));

		verify(subcategoryService, times(1)).getSubcategoryList(ids);
	}

	@Test
	@DisplayName("존재하지 않는 Subcategory Id 쿼리가 들어올 경우, SubcategoryNotFound Error Response를 반환하는지 테스트")
	public void testGetTagsByidsWithValidQueryButNotExist() throws Exception {
		//given
		when(subcategoryService.getSubcategoryList(anyList())).thenThrow(new SubcategoryNotFoundException());

		//when & then
		mockMvc.perform(get("/v1/tags")
				.param("subcategories", "999")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.isSuccess", is(false)))
			.andExpect(jsonPath("$.code", is(ErrorCode.SUBCATEGORY_ID_NOT_FOUND.getCode())))
			.andExpect(jsonPath("$.data.message", is(ErrorCode.SUBCATEGORY_ID_NOT_FOUND.getMessage())))
			.andExpect(jsonPath("$.timestamp", notNullValue()));
	}

	@Test
	@DisplayName("올바르지 않은 쿼리가 들어올 경우, INVALID_QUERY_FORMAT을 반환하는지 테스트")
	public void testGetTagsByidsWithInvalidQueryFormat() throws Exception {
		//given
		when(subcategoryService.getSubcategoryList(anyList())).thenThrow(MethodArgumentTypeMismatchException.class);

		//when & then
		mockMvc.perform(get("/v1/tags")
				.param("subcategories", "1,2")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.isSuccess", is(false)))
			.andExpect(jsonPath("$.code", is(ErrorCode.QUERY_TYPE_MISMATCH.getCode())))
			.andExpect(jsonPath("$.data.message", is(ErrorCode.QUERY_TYPE_MISMATCH.getMessage())))
			.andExpect(jsonPath("$.timestamp", notNullValue()));
	}
}
