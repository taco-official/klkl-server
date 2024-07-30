package taco.klkl.domain.subcategory.integration;

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
import taco.klkl.domain.subcategory.dto.response.SubcategoryResponseDto;
import taco.klkl.domain.subcategory.sevice.SubcategoryService;
import taco.klkl.global.error.exception.ErrorCode;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class SubcategoryIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private SubcategoryService subcategoryService;

	@Test
	@DisplayName("valid한 id값이 들어왔을 때 반환값이 제대로 전달되는지 테스트")
	public void testGetCategoriesWithValidId() throws Exception {
		//given
		List<SubcategoryResponseDto> subcategoryResponseDto = subcategoryService.getSubcategories(300L);

		//when, then
		mockMvc.perform(get("/v1/categories/300/subcategories")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data", hasSize(subcategoryResponseDto.size())))
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
