package taco.klkl.domain.comment.integration;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static taco.klkl.global.common.constants.TestConstants.TEST_UUID;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import taco.klkl.domain.comment.dto.request.CommentCreateUpdateRequest;
import taco.klkl.domain.comment.dto.response.CommentResponse;
import taco.klkl.domain.comment.service.CommentService;
import taco.klkl.domain.token.service.TokenProvider;
import taco.klkl.global.config.security.TestSecurityConfig;
import taco.klkl.global.error.exception.ErrorCode;
import taco.klkl.global.util.ResponseUtil;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Import(TestSecurityConfig.class)
@WithMockUser(username = TEST_UUID, roles = "USER")
@ActiveProfiles("test")
public class CommentIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private TokenProvider tokenProvider;

	@MockBean
	private ResponseUtil responseUtil;

	@Autowired
	private CommentService commentService;

	@Autowired
	private ObjectMapper objectMapper;

	private final Long productId = 101L;
	private final Long commentId = 500L;

	private final CommentCreateUpdateRequest commentCreateRequestDto = new CommentCreateUpdateRequest(
		"createdContent"
	);

	private final CommentCreateUpdateRequest commentUpdateRequestDto = new CommentCreateUpdateRequest(
		"updatedContent"
	);

	@Test
	@DisplayName("상품에 있는 모든 댓글 반환 통합 테스트")
	public void testGetComment() throws Exception {
		//given
		List<CommentResponse> commentResponses = commentService.findCommentsByProductId(productId);

		//when & then
		mockMvc.perform(get("/v1/products/{productId}/comments", productId)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.isSuccess", is(true)))
			.andExpect(jsonPath("$.data", hasSize(commentResponses.size())));
	}

	@Test
	@DisplayName("댓글 작성 성공 테스트")
	public void testCreateComment() throws Exception {
		//given

		//when & then
		mockMvc.perform(post("/v1/products/{productId}/comments", productId)
				.content(objectMapper.writeValueAsString(commentCreateRequestDto))
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.isSuccess", is(true)))
			.andExpect(jsonPath("$.data.id", is(1)))
			.andExpect(jsonPath("$.data.content", is(commentCreateRequestDto.content())));
	}

	@Test
	@DisplayName("댓글 등록시 존재하지 않는 상품이라 실패하는 경우 테스트")
	public void testCreateCommentWhenProductNotFound() throws Exception {
		//given
		Long wrongProductId = 2L;

		//when & then
		mockMvc.perform(post("/v1/products/{wrongProductId}/comments", wrongProductId)
				.content(objectMapper.writeValueAsString(commentCreateRequestDto))
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.isSuccess", is(false)))
			.andExpect(jsonPath("$.status", is(ErrorCode.PRODUCT_NOT_FOUND.getHttpStatus().value())))
			.andExpect(jsonPath("$.data.message", is(ErrorCode.PRODUCT_NOT_FOUND.getMessage())));
	}

	@Test
	@DisplayName("댓글 수정 성공 테스트")
	public void testUpdateComment() throws Exception {
		//given

		//when & then
		mockMvc.perform(put("/v1/products/{productId}/comments/{commentId}", productId, commentId)
				.content(objectMapper.writeValueAsString(commentUpdateRequestDto))
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.isSuccess", is(true)))
			.andExpect(jsonPath("$.data.id", is(commentId.intValue())))
			.andExpect(jsonPath("$.data.content", is(commentUpdateRequestDto.content())));
	}

	@Test
	@DisplayName("댓글 수정시 존재하지 않는 댓글이라 실패하는 경우 테스트")
	public void testUpdateCommentWhenCommentNotFound() throws Exception {
		//given
		Long wrongCommentId = 1L;

		//when & then
		mockMvc.perform(put("/v1/products/{productId}/comments/{wrongCommentId}", productId, wrongCommentId)
				.content(objectMapper.writeValueAsString(commentUpdateRequestDto))
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.isSuccess", is(false)))
			.andExpect(jsonPath("$.status", is(ErrorCode.COMMENT_NOT_FOUND.getHttpStatus().value())))
			.andExpect(jsonPath("$.data.message", is(ErrorCode.COMMENT_NOT_FOUND.getMessage())));
	}

	@Test
	@DisplayName("댓글 수정시 존재하지 않는 상품이라 실패하는 경우 테스트")
	public void testUpdateCommentWhenProductNotFound() throws Exception {
		//given
		Long wrongProductId = 2L;

		//when & then
		mockMvc.perform(put("/v1/products/{wrongProductId}/comments/{commentId}", wrongProductId, commentId)
				.content(objectMapper.writeValueAsString(commentUpdateRequestDto))
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.isSuccess", is(false)))
			.andExpect(jsonPath("$.status", is(ErrorCode.PRODUCT_NOT_FOUND.getHttpStatus().value())))
			.andExpect(jsonPath("$.data.message", is(ErrorCode.PRODUCT_NOT_FOUND.getMessage())));
	}

	@Test
	@DisplayName("댓글 수정시 존재하는 상품이지만 댓글에 저장된 상품 Id와 달라 실패하는 경우 테스트")
	public void testUpdateCommentWhenExistProductButNotMatchWithComment() throws Exception {
		//given
		Long differentProductId = 102L;

		//when & given
		mockMvc.perform(put("/v1/products/{wrongProductId}/comments/{commentId}", differentProductId, commentId)
				.content(objectMapper.writeValueAsString(commentUpdateRequestDto))
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.isSuccess", is(false)))
			.andExpect(jsonPath("$.status", is(ErrorCode.COMMENT_PRODUCT_NOT_MATCH.getHttpStatus().value())))
			.andExpect(jsonPath("$.data.message", is(ErrorCode.COMMENT_PRODUCT_NOT_MATCH.getMessage())));
	}

	@Test
	@DisplayName("댓글 삭제 성공 테스트")
	public void testDeleteComment() throws Exception {
		// given

		// when & then
		mockMvc.perform(delete("/v1/products/{productId}/comments/{commentId}", productId, commentId)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNoContent())
			.andExpect(jsonPath("$.isSuccess", is(true)))
			.andExpect(jsonPath("$.data", nullValue()));
	}

	@Test
	@DisplayName("댓글 삭제시 존재하지 않는 댓글이라 실패하는 경우 테스트")
	public void testDeleteCommentWhenCommentNotFound() throws Exception {
		//given
		Long wrongCommentId = 1L;

		//when & then
		mockMvc.perform(delete("/v1/products/{productId}/comments/{wrongCommentId}", productId, wrongCommentId)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.isSuccess", is(false)))
			.andExpect(jsonPath("$.status", is(ErrorCode.COMMENT_NOT_FOUND.getHttpStatus().value())))
			.andExpect(jsonPath("$.data.message", is(ErrorCode.COMMENT_NOT_FOUND.getMessage())));
	}

	@Test
	@DisplayName("댓글 삭제시 존재하지 않는 상품이라 실패하는 경우 테스트")
	public void testDeleteCommentWhenProductNotFound() throws Exception {
		//given
		Long wrongProductId = 2L;

		//when & then
		mockMvc.perform(delete("/v1/products/{wrongProductId}/comments/{commentId}", wrongProductId, commentId)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.isSuccess", is(false)))
			.andExpect(jsonPath("$.status", is(ErrorCode.PRODUCT_NOT_FOUND.getHttpStatus().value())))
			.andExpect(jsonPath("$.data.message", is(ErrorCode.PRODUCT_NOT_FOUND.getMessage())));
	}

	@Test
	@DisplayName("댓글 삭제시 존재하는 상품이지만 댓글에 저장된 상품 Id와 달라 실패하는 경우 테스트")
	public void testDeleteCommentWhenExistProductButNotMatchWithComment() throws Exception {
		//given
		Long differentProductId = 102L;

		//when & given
		mockMvc.perform(delete("/v1/products/{wrongProductId}/comments/{commentId}", differentProductId, commentId)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.isSuccess", is(false)))
			.andExpect(jsonPath("$.status", is(ErrorCode.COMMENT_PRODUCT_NOT_MATCH.getHttpStatus().value())))
			.andExpect(jsonPath("$.data.message", is(ErrorCode.COMMENT_PRODUCT_NOT_MATCH.getMessage())));
	}
}
