package taco.klkl.domain.category.integration.tag;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import taco.klkl.domain.category.domain.subcategory.Subcategory;
import taco.klkl.domain.category.dto.response.tag.TagResponse;
import taco.klkl.domain.category.service.SubcategoryTagService;
import taco.klkl.domain.category.service.subcategory.SubcategoryService;
import taco.klkl.global.error.exception.ErrorCode;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class TagIntegrationTest {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private SubcategoryService subcategoryService;

	@Autowired
	private SubcategoryTagService subcategoryTagService;

	@Test
	@DisplayName("존재하는 Subcategory Id 쿼리가 들어왔을 경우, API 테스트")
	public void testGetFilterApiWithValidQuery() throws Exception {
		//given
		List<String> subcategoryIds = Arrays.asList("310", "311", "312");
		String subcategoryIdsParam = String.join(",", subcategoryIds);

		//when
		List<Subcategory> subcategoryList = subcategoryService.findSubcategoriesBySubcategoryIds(
			subcategoryIds.stream()
				.map(Long::parseLong)
				.toList()
		);
		Set<TagResponse> response =
			subcategoryTagService.findTagsBySubcategoryList(subcategoryList);

		//then
		mockMvc.perform(get("/v1/tags")
				.param("subcategory_id", subcategoryIdsParam)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.isSuccess", is(true)))
			.andExpect(jsonPath("$.data", hasSize(response.size())));
	}

	@Test
	@DisplayName("존재하는 Subcategory Id 쿼리가 들어왔지만 Filter가 없는 Subcategory일 경우, Api 테스트")
	public void testGetFilterApiWithValidQueryButEmptyOne() throws Exception {
		//given
		List<String> subcategoryIds = Arrays.asList("310", "311", "312", "315");
		String subcategoryIdsParam = String.join(",", subcategoryIds);

		//when
		List<Subcategory> subcategoryList = subcategoryService.findSubcategoriesBySubcategoryIds(
			subcategoryIds.stream()
				.map(Long::parseLong)
				.toList()
		);
		Set<TagResponse> response =
			subcategoryTagService.findTagsBySubcategoryList(subcategoryList);

		//then
		mockMvc.perform(get("/v1/tags")
				.param("subcategory_id", subcategoryIdsParam)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.isSuccess", is(true)))
			.andExpect(jsonPath("$.data", hasSize(response.size())));
	}

	@Test
	@DisplayName("존재하는 않는 Subcategory Id 쿼리가 들어올 경우, Api 테스트")
	public void testGetFilterApiWithValidQueryButNotExist() throws Exception {
		//given
		List<String> subcategoryIds = Arrays.asList("310", "311", "312", "400");
		String subcategoryIdsParam = String.join(",", subcategoryIds);

		//then
		mockMvc.perform(get("/v1/tags")
				.param("subcategory_id", subcategoryIdsParam)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.isSuccess", is(false)))
			.andExpect(jsonPath("$.status", is(ErrorCode.SUBCATEGORY_NOT_FOUND.getHttpStatus().value())))
			.andExpect(jsonPath("$.data.message", is(ErrorCode.SUBCATEGORY_NOT_FOUND.getMessage())));
	}

	@Test
	@DisplayName("올바르지 않은 쿼리가 들어올 경우, Api 테스트")
	public void testGetFilterApiWithInvalidQueryFormat() throws Exception {
		//given
		List<String> subcategoryIds = Arrays.asList("310", "311", "312", "asdf");
		String subcategoryIdsParam = String.join(",", subcategoryIds);

		//then
		mockMvc.perform(get("/v1/tags")
				.param("subcategory_id", subcategoryIdsParam)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.isSuccess", is(false)))
			.andExpect(jsonPath("$.status", is(ErrorCode.QUERY_TYPE_MISMATCH.getHttpStatus().value())))
			.andExpect(jsonPath("$.data.message", is(ErrorCode.QUERY_TYPE_MISMATCH.getMessage())));
	}
}
