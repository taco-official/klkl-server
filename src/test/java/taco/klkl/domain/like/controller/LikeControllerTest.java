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
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import taco.klkl.domain.like.dto.response.LikeResponse;
import taco.klkl.domain.like.exception.LikeCountBelowMinimumException;
import taco.klkl.domain.like.exception.LikeCountOverMaximumException;
import taco.klkl.domain.like.service.LikeService;
import taco.klkl.domain.token.service.TokenProvider;
import taco.klkl.global.config.security.TestSecurityConfig;
import taco.klkl.global.error.exception.ErrorCode;
import taco.klkl.global.util.ResponseUtil;
import taco.klkl.global.util.TokenUtil;

@WebMvcTest(LikeController.class)
@Import(TestSecurityConfig.class)
@ActiveProfiles("test")
class LikeControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private TokenProvider tokenProvider;

	@MockBean
	private ResponseUtil responseUtil;

	@MockBean
	private TokenUtil tokenUtil;

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
		LikeResponse likeResponse = LikeResponse.of(true, 1);
		when(likeService.createLike(productId)).thenReturn(likeResponse);

		// when & then
		mockMvc.perform(post("/v1/likes/{productId}", productId))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.isSuccess", is(true)))
			.andExpect(jsonPath("$.data.isLiked", is(true)))
			.andExpect(jsonPath("$.data.likeCount", is(1)));

		verify(likeService).createLike(productId);
	}

	@Test
	@DisplayName("좋아요 DELETE 테스트")
	void testDeleteLike() throws Exception {
		// given
		Long productId = 1L;
		LikeResponse likeResponse = LikeResponse.of(false, 1);
		when(likeService.deleteLike(productId)).thenReturn(likeResponse);

		// when & then
		mockMvc.perform(delete("/v1/likes/{productId}", productId))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.isSuccess", is(true)))
			.andExpect(jsonPath("$.data.isLiked", is(false)))
			.andExpect(jsonPath("$.data.likeCount", is(1)));

		verify(likeService).deleteLike(productId);
	}

	@Test
	@DisplayName("좋아요 POST 최대값 에러 테스트")
	void testPostLikeMaximumError() throws Exception {
		// given
		Long productId = 1L;
		LikeCountOverMaximumException exception = new LikeCountOverMaximumException();
		doThrow(exception).when(likeService).createLike(productId);

		// when & then
		mockMvc.perform(post("/v1/likes/{productId}", productId))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.isSuccess", is(false)))
			.andExpect(jsonPath("$.status", is(ErrorCode.LIKE_COUNT_OVER_MAXIMUM.getHttpStatus().value())))
			.andExpect(jsonPath("$.data.message", is(ErrorCode.LIKE_COUNT_OVER_MAXIMUM.getMessage())));

		verify(likeService).createLike(productId);
	}

	@Test
	@DisplayName("좋아요 DELETE 최소값 에러 테스트")
	void testDeleteLikeMaximumError() throws Exception {
		// given
		Long productId = 1L;
		LikeCountBelowMinimumException exception = new LikeCountBelowMinimumException();
		doThrow(exception).when(likeService).deleteLike(productId);

		// when & then
		mockMvc.perform(delete("/v1/likes/{productId}", productId))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.isSuccess", is(false)))
			.andExpect(jsonPath("$.status", is(ErrorCode.LIKE_COUNT_BELOW_MINIMUM.getHttpStatus().value())))
			.andExpect(jsonPath("$.data.message", is(ErrorCode.LIKE_COUNT_BELOW_MINIMUM.getMessage())));

		verify(likeService).deleteLike(productId);
	}
}
