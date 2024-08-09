package taco.klkl.domain.comment.controller;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import taco.klkl.domain.comment.domain.Comment;
import taco.klkl.domain.comment.dto.request.CommentCreateUpdateRequestDto;
import taco.klkl.domain.comment.dto.response.CommentResponseDto;
import taco.klkl.domain.comment.exception.CommentNotFoundException;
import taco.klkl.domain.comment.service.CommentService;
import taco.klkl.domain.product.exception.ProductNotFoundException;
import taco.klkl.domain.product.service.ProductService;
import taco.klkl.domain.user.dao.UserRepository;
import taco.klkl.domain.user.domain.User;
import taco.klkl.domain.user.dto.request.UserCreateRequestDto;
import taco.klkl.global.common.enums.Gender;
import taco.klkl.global.common.validation.UserIdValidator;
import taco.klkl.global.error.exception.ErrorCode;

@WebMvcTest(CommentController.class)
public class CommentControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private CommentService commentService;

	@MockBean
	private ProductService productService;

	@MockBean
	private UserRepository userRepository;

	@MockBean
	private UserIdValidator userIdValidator;

	@Autowired
	private ObjectMapper objectMapper;

	private final Long productId = 1L;
	private final Long commentId = 1L;

	private final UserCreateRequestDto requestDto = new UserCreateRequestDto(
		"이상화",
		"남",
		19,
		"image/ideal-flower.jpg",
		"저는 이상화입니다."
	);

	private final User user = User.of(
		requestDto.profile(),
		requestDto.name(),
		Gender.getGenderByDescription(requestDto.description()),
		requestDto.age(),
		requestDto.description()
	);

	private final Comment comment1 = Comment.of(1L, user, "개추 ^^");
	private final Comment comment2 = Comment.of(1L, user, "안녕하세요");

	private final CommentCreateUpdateRequestDto commentCreateRequestDto = new CommentCreateUpdateRequestDto(
		1L,
		"개추 ^^"
	);

	private final CommentCreateUpdateRequestDto commentUpdateRequestDto = new CommentCreateUpdateRequestDto(
		1L,
		"윤상정은 바보다, 반박시 님 말이 틀림."
	);

	@Test
	@DisplayName("상품에 있는 모든 댓글 반환 테스트")
	public void testGetComment() throws Exception {
		//given
		List<CommentResponseDto> responseDtos = Arrays.asList(CommentResponseDto.from(comment1),
			CommentResponseDto.from(comment2));

		when(commentService.getComments(productId)).thenReturn(responseDtos);

		//when & then
		mockMvc.perform(get("/v1/products/{productId}/comments", productId)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.isSuccess", is(true)))
			.andExpect(jsonPath("$.code", is("C000")))
			.andExpect(jsonPath("$.data", hasSize(2)))
			.andExpect(jsonPath("$.data[0].commentId", is(comment1.getId())))
			.andExpect(jsonPath("$.data[0].content", is(comment1.getContent())))
			.andExpect(jsonPath("$.data[1].commentId", is(comment2.getId())))
			.andExpect(jsonPath("$.data[1].content", is(comment2.getContent())));

		verify(commentService, times(1))
			.getComments(productId);
	}

	@Test
	@DisplayName("댓글 등록 성공 테스트")
	public void testCreateComment() throws Exception {
		//given
		when(userRepository.existsById(any(Long.class))).thenReturn(true);

		CommentResponseDto responseDto = CommentResponseDto.from(comment1);

		when(commentService.createComment(any(Long.class), any(CommentCreateUpdateRequestDto.class))).thenReturn(
			responseDto);

		//when & then
		mockMvc.perform(post("/v1/products/{productId}/comments", productId)
				.content(objectMapper.writeValueAsString(commentCreateRequestDto))
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.isSuccess", is(true)))
			.andExpect(jsonPath("$.code", is("C000")))
			.andExpect(jsonPath("$.data.commentId", is(comment1.getId())))
			.andExpect(jsonPath("$.data.userId", is(comment1.getUser().getId())))
			.andExpect(jsonPath("$.data.content", is(comment1.getContent())));

		verify(commentService, times(1))
			.createComment(productId, commentCreateRequestDto);
	}

	@Test
	@DisplayName("댓글 등록시 존재하지 않는 상품이라 실패하는 경우 테스트")
	public void testCreateCommentWhenProductNotFound() throws Exception {
		//given
		Long wrongProductId = 2L;
		when(userRepository.existsById(any(Long.class))).thenReturn(true);

		doThrow(new ProductNotFoundException()).when(productService).validateProductId(any(Long.class));

		//when & then
		mockMvc.perform(post("/v1/products/{wrongProductId}/comments", wrongProductId)
				.content(objectMapper.writeValueAsString(commentCreateRequestDto))
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.isSuccess", is(false)))
			.andExpect(jsonPath("$.code", is(ErrorCode.PRODUCT_NOT_FOUND.getCode())))
			.andExpect(jsonPath("$.data.message", is(ErrorCode.PRODUCT_NOT_FOUND.getMessage())));
	}

	@Test
	@DisplayName("댓글 수정 성공 테스트")
	public void testUpdateComment() throws Exception {
		///given
		when(userRepository.existsById(any(Long.class))).thenReturn(true);

		CommentResponseDto responseDto = CommentResponseDto.from(comment1);

		when(commentService.updateComment(
			any(Long.class),
			any(Long.class),
			any(CommentCreateUpdateRequestDto.class)))
			.thenReturn(responseDto);

		//when & then
		mockMvc.perform(put("/v1/products/{productId}/comments/{commentId}", productId, commentId)
				.content(objectMapper.writeValueAsString(commentUpdateRequestDto))
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.isSuccess", is(true)))
			.andExpect(jsonPath("$.code", is("C000")))
			.andExpect(jsonPath("$.data.commentId", is(comment1.getId())))
			.andExpect(jsonPath("$.data.userId", is(comment1.getUser().getId())))
			.andExpect(jsonPath("$.data.content", is(comment1.getContent())));

		verify(commentService, times(1))
			.updateComment(productId, commentId, commentUpdateRequestDto);
	}

	@Test
	@DisplayName("댓글 수정시 존재하지 않는 댓글이라 실패하는 경우 테스트")
	public void testUpdateCommentWhenCommentNotFound() throws Exception {
		///given
		Long wrongCommentId = 2L;
		when(userRepository.existsById(any(Long.class))).thenReturn(true);

		when(commentService.updateComment(any(Long.class), any(Long.class), any(CommentCreateUpdateRequestDto.class)))
			.thenThrow(new CommentNotFoundException());

		//when & then
		mockMvc.perform(put("/v1/products/{productId}/comments/{wrongCommentId}", productId, wrongCommentId)
				.content(objectMapper.writeValueAsString(commentUpdateRequestDto))
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.isSuccess", is(false)))
			.andExpect(jsonPath("$.code", is(ErrorCode.COMMENT_NOT_FOUND.getCode())))
			.andExpect(jsonPath("$.data.message", is(ErrorCode.COMMENT_NOT_FOUND.getMessage())));

		verify(commentService, times(1))
			.updateComment(productId, wrongCommentId, commentUpdateRequestDto);
	}

	@Test
	@DisplayName("댓글 수정시 존재하지 않는 상품이라 실패하는 경우 테스트")
	public void testUpdateCommentWhenProductNotFound() throws Exception {
		//given
		Long wrongProductId = 2L;
		when(userRepository.existsById(any(Long.class))).thenReturn(true);

		when(commentService.updateComment(any(Long.class), any(Long.class), any(CommentCreateUpdateRequestDto.class)))
			.thenThrow(new ProductNotFoundException());

		//when & then
		mockMvc.perform(put("/v1/products/{wrongProductId}/comments/{commentId}", wrongProductId, commentId)
				.content(objectMapper.writeValueAsString(commentUpdateRequestDto))
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.isSuccess", is(false)))
			.andExpect(jsonPath("$.code", is(ErrorCode.PRODUCT_NOT_FOUND.getCode())))
			.andExpect(jsonPath("$.data.message", is(ErrorCode.PRODUCT_NOT_FOUND.getMessage())));

		verify(commentService, times(1))
			.updateComment(wrongProductId, commentId, commentUpdateRequestDto);
	}

	@Test
	@DisplayName("댓글 삭제 성공 테스트")
	public void testDeleteComment() throws Exception {
		//given
		when(userRepository.existsById(any(Long.class))).thenReturn(true);

		//when & then
		mockMvc.perform(delete("/v1/products/{productId}/comments/{commentId}", productId, commentId))
			.andExpect(status().isNoContent())
			.andExpect(jsonPath("$.isSuccess", is(true)))
			.andExpect(jsonPath("$.code", is("C000")))
			.andExpect(jsonPath("$.data", nullValue()));

		verify(commentService, times(1)).deleteComment(productId, commentId);
	}

	@Test
	@DisplayName("댓글 삭제시 존재하지 않는 댓글이라 실패하는 경우 테스트")
	public void testDeleteCommentWhenCommentNotFound() throws Exception {
		//given
		Long wrongCommentId = 2L;
		when(userRepository.existsById(any(Long.class))).thenReturn(true);

		doThrow(new CommentNotFoundException()).when(commentService).deleteComment(productId, wrongCommentId);

		//when & then
		mockMvc.perform(delete("/v1/products/{productId}/comments/{wrongCommentId}", productId, wrongCommentId)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.isSuccess", is(false)))
			.andExpect(jsonPath("$.code", is(ErrorCode.COMMENT_NOT_FOUND.getCode())))
			.andExpect(jsonPath("$.data.message", is(ErrorCode.COMMENT_NOT_FOUND.getMessage())));

		verify(commentService, times(1)).deleteComment(productId, wrongCommentId);
	}

	@Test
	@DisplayName("댓글 삭제시 존재하지 않는 상품이라 실패하는 경우 테스트")
	public void testDeleteCommentWhenProductNotFound() throws Exception {
		//given
		Long wrongProductId = 2L;
		when(userRepository.existsById(any(Long.class))).thenReturn(true);

		doThrow(new ProductNotFoundException()).when(commentService).deleteComment(wrongProductId, commentId);

		//when & then
		mockMvc.perform(delete("/v1/products/{wrongProductId}/comments/{commentId}", wrongProductId, commentId)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.isSuccess", is(false)))
			.andExpect(jsonPath("$.code", is(ErrorCode.PRODUCT_NOT_FOUND.getCode())))
			.andExpect(jsonPath("$.data.message", is(ErrorCode.PRODUCT_NOT_FOUND.getMessage())));

		verify(commentService, times(1)).deleteComment(wrongProductId, commentId);
	}
}
