package taco.klkl.domain.like.controller;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import taco.klkl.domain.like.dto.response.LikeResponseDto;
import taco.klkl.domain.like.exception.LikeCountMaximumException;
import taco.klkl.domain.like.exception.LikeCountMinimumException;
import taco.klkl.domain.like.service.LikeService;

@WebMvcTest(LikeController.class)
class LikeControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private LikeService likeService;

	@InjectMocks
	private LikeController likeController;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	@DisplayName("좋아요 POST 테스트")
	void testPostLike() throws Exception {
		// given
		Long productId = 1L;
		LikeResponseDto likeResponseDto = LikeResponseDto.of(true, 1);
		when(likeService.createLike(productId)).thenReturn(likeResponseDto);

		// when & then
		mockMvc.perform(post("/v1/products/{productId}/likes", productId))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.isLiked", is(true)))
			.andExpect(jsonPath("$.data.likeCount", is(1)));

		verify(likeService).createLike(productId);
	}

	@Test
	@DisplayName("좋아요 DELETE 테스트")
	void testDeleteLike() throws Exception {
		// given
		Long productId = 1L;
		LikeResponseDto likeResponseDto = LikeResponseDto.of(false, 1);
		when(likeService.deleteLike(productId)).thenReturn(likeResponseDto);

		// when & then
		mockMvc.perform(delete("/v1/products/{productId}/likes", productId))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.isLiked", is(false)))
			.andExpect(jsonPath("$.data.likeCount", is(1)));

		verify(likeService).deleteLike(productId);
	}

	@Test
	@DisplayName("좋아요 POST 최대값 에러 테스트")
	void testPostLikeMaximumError() throws Exception {
		// given
		Long productId = 1L;
		LikeCountMaximumException exception = new LikeCountMaximumException();
		doThrow(exception).when(likeService).createLike(productId);

		// when & then
		mockMvc.perform(post("/v1/products/{productId}/likes", productId))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.code", is(exception.getErrorCode().getCode())))
			.andExpect(jsonPath("$.data.message", is(exception.getMessage())))
			.andExpect(jsonPath("$.timestamp", notNullValue()));

		verify(likeService).createLike(productId);
	}

	@Test
	@DisplayName("좋아요 DELETE 최소값 에러 테스트")
	void testDeleteLikeMaximumError() throws Exception {
		// given
		Long productId = 1L;
		LikeCountMinimumException exception = new LikeCountMinimumException();
		doThrow(exception).when(likeService).deleteLike(productId);

		// when & then
		mockMvc.perform(delete("/v1/products/{productId}/likes", productId))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.code", is(exception.getErrorCode().getCode())))
			.andExpect(jsonPath("$.data.message", is(exception.getMessage())))
			.andExpect(jsonPath("$.timestamp", notNullValue()));

		verify(likeService).deleteLike(productId);
	}
}
