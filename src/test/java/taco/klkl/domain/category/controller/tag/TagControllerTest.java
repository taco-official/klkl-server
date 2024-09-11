package taco.klkl.domain.category.controller.tag;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import taco.klkl.domain.category.dao.SubcategoryTagRepository;
import taco.klkl.domain.category.domain.SubcategoryTag;
import taco.klkl.domain.category.domain.subcategory.Subcategory;
import taco.klkl.domain.category.domain.subcategory.SubcategoryType;
import taco.klkl.domain.category.domain.tag.Tag;
import taco.klkl.domain.category.domain.tag.TagType;
import taco.klkl.domain.category.dto.response.tag.TagResponse;
import taco.klkl.domain.category.exception.subcategory.SubcategoryNotFoundException;
import taco.klkl.domain.category.service.SubcategoryTagService;
import taco.klkl.domain.category.service.subcategory.SubcategoryService;
import taco.klkl.global.error.exception.ErrorCode;

@WebMvcTest(TagController.class)
public class TagControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private SubcategoryService subcategoryService;

	@MockBean
	private SubcategoryTagService subcategoryTagService;

	@MockBean
	private SubcategoryTagRepository subcategoryTagRepository;

	@Test
	@DisplayName("존재하는 Subcategory Id 쿼리가 들어왔을 경우, Tag가 잘 나오는지 테스트")
	public void testGetTagsByIdsWithValidQuery() throws Exception {
		// given
		List<Long> subcategoryIds = Arrays.asList(1L, 2L);

		Subcategory mockSubcategory1 = mock(Subcategory.class);
		Subcategory mockSubcategory2 = mock(Subcategory.class);
		Tag mockTag1 = mock(Tag.class);
		Tag mockTag2 = mock(Tag.class);

		SubcategoryTag subcategoryTag1 = SubcategoryTag.of(mockSubcategory1, mockTag1);
		SubcategoryTag subcategoryTag2 = SubcategoryTag.of(mockSubcategory1, mockTag2);
		SubcategoryTag subcategoryTag3 = SubcategoryTag.of(mockSubcategory2, mockTag1);

		List<Subcategory> mockSubcategoryList = Arrays.asList(mockSubcategory1, mockSubcategory2);

		when(mockSubcategory1.getId()).thenReturn(1L);
		when(mockSubcategory1.getName()).thenReturn(SubcategoryType.INSTANT_FOOD.getName());
		when(mockSubcategory2.getId()).thenReturn(2L);
		when(mockSubcategory2.getName()).thenReturn(SubcategoryType.SNACK.getName());

		when(mockTag1.getId()).thenReturn(1L);
		when(mockTag1.getName()).thenReturn(TagType.CONVENIENCE_STORE.getName());
		when(mockTag2.getId()).thenReturn(2L);
		when(mockTag2.getName()).thenReturn(TagType.CILANTRO.getName());

		when(subcategoryTagRepository.findAllBySubcategory(mockSubcategory1))
			.thenReturn(Arrays.asList(subcategoryTag1, subcategoryTag2));
		when(subcategoryTagRepository.findAllBySubcategory(mockSubcategory2))
			.thenReturn(Collections.singletonList(subcategoryTag3));

		Set<TagResponse> expectedResponse = new HashSet<>(Arrays.asList(
			TagResponse.from(mockTag1),
			TagResponse.from(mockTag2)
		));

		when(subcategoryService.findSubcategoriesBySubcategoryIds(subcategoryIds)).thenReturn(mockSubcategoryList);
		when(subcategoryTagService.findTagsBySubcategoryList(mockSubcategoryList)).thenReturn(expectedResponse);

		// when & then
		mockMvc.perform(get("/v1/tags")
				.param("subcategory_id", "1,2")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.isSuccess", is(true)))
			.andExpect(jsonPath("$.data", hasSize(2)))
			.andExpect(jsonPath("$.data[*].id", containsInAnyOrder(1, 2)))
			.andExpect(jsonPath("$.data[*].name", containsInAnyOrder(
				TagType.CONVENIENCE_STORE.getName(),
				TagType.CILANTRO.getName()
			)))
			.andExpect(jsonPath("$.timestamp", notNullValue()));

		verify(subcategoryService, times(1)).findSubcategoriesBySubcategoryIds(subcategoryIds);
		verify(subcategoryTagService, times(1)).findTagsBySubcategoryList(mockSubcategoryList);
	}

	@Test
	@DisplayName("존재하는 Subcategory Id 쿼리가 들어왔지만 Tag가 없는 Subcategory일 경우, Tag가 잘 나오는지 테스트")
	public void testGetTagsByIdsWithValidQueryButEmptyOne() throws Exception {
		// given
		List<Long> ids = Arrays.asList(1L, 2L);

		Subcategory mockSubcategory1 = mock(Subcategory.class);
		Subcategory mockSubcategory2 = mock(Subcategory.class);
		Tag mockTag1 = mock(Tag.class);
		Tag mockTag2 = mock(Tag.class);

		SubcategoryTag subcategoryTag1 = SubcategoryTag.of(mockSubcategory1, mockTag1);
		SubcategoryTag subcategoryTag2 = SubcategoryTag.of(mockSubcategory1, mockTag2);

		List<Subcategory> mockSubcategoryList = Arrays.asList(mockSubcategory1, mockSubcategory2);

		when(mockSubcategory1.getId()).thenReturn(1L);
		when(mockSubcategory1.getName()).thenReturn(SubcategoryType.INSTANT_FOOD.getName());
		when(mockSubcategory2.getId()).thenReturn(2L);
		when(mockSubcategory2.getName()).thenReturn(SubcategoryType.SNACK.getName());

		when(mockTag1.getId()).thenReturn(1L);
		when(mockTag1.getName()).thenReturn(TagType.CONVENIENCE_STORE.getName());
		when(mockTag2.getId()).thenReturn(2L);
		when(mockTag2.getName()).thenReturn(TagType.CILANTRO.getName());

		when(subcategoryTagRepository.findAllBySubcategory(mockSubcategory1))
			.thenReturn(Arrays.asList(subcategoryTag1, subcategoryTag2));
		when(subcategoryTagRepository.findAllBySubcategory(mockSubcategory2))
			.thenReturn(Collections.emptyList());

		Set<TagResponse> expectedResponse = new HashSet<>(Arrays.asList(
			TagResponse.from(mockTag1),
			TagResponse.from(mockTag2)
		));

		when(subcategoryService.findSubcategoriesBySubcategoryIds(ids)).thenReturn(mockSubcategoryList);
		when(subcategoryTagService.findTagsBySubcategoryList(mockSubcategoryList)).thenReturn(expectedResponse);

		// when & then
		mockMvc.perform(get("/v1/tags")
				.param("subcategory_id", "1,2")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.isSuccess", is(true)))
			.andExpect(jsonPath("$.data", hasSize(2)))
			.andExpect(jsonPath("$.data[*].id", containsInAnyOrder(1, 2)))
			.andExpect(jsonPath("$.data[*].name", containsInAnyOrder(
				TagType.CONVENIENCE_STORE.getName(),
				TagType.CILANTRO.getName()
			)))
			.andExpect(jsonPath("$.timestamp", notNullValue()));

		verify(subcategoryService, times(1)).findSubcategoriesBySubcategoryIds(ids);
		verify(subcategoryTagService, times(1)).findTagsBySubcategoryList(mockSubcategoryList);
	}

	@Test
	@DisplayName("존재하지 않는 Subcategory Id 쿼리가 들어올 경우, SubcategoryNotFound Error Response를 반환하는지 테스트")
	public void testGetTagsByIdsWithValidQueryButNotExist() throws Exception {
		//given
		when(subcategoryService.findSubcategoriesBySubcategoryIds(anyList()))
			.thenThrow(new SubcategoryNotFoundException());

		//when & then
		mockMvc.perform(get("/v1/tags")
				.param("subcategory_id", "999")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.isSuccess", is(false)))
			.andExpect(jsonPath("$.status", is(ErrorCode.SUBCATEGORY_NOT_FOUND.getHttpStatus().value())))
			.andExpect(jsonPath("$.data.message", is(ErrorCode.SUBCATEGORY_NOT_FOUND.getMessage())));
	}

	@Test
	@DisplayName("올바르지 않은 쿼리가 들어올 경우, INVALID_QUERY_FORMAT을 반환하는지 테스트")
	public void testGetTagsByIdsWithInvalidQueryFormat() throws Exception {
		//given
		when(subcategoryService.findSubcategoriesBySubcategoryIds(anyList()))
			.thenThrow(MethodArgumentTypeMismatchException.class);

		//when & then
		mockMvc.perform(get("/v1/tags")
				.param("subcategory_id", "1,2")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.isSuccess", is(false)))
			.andExpect(jsonPath("$.status", is(ErrorCode.QUERY_TYPE_MISMATCH.getHttpStatus().value())))
			.andExpect(jsonPath("$.data.message", is(ErrorCode.QUERY_TYPE_MISMATCH.getMessage())));
	}
}
