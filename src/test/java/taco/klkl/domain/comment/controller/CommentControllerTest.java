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

import taco.klkl.domain.category.domain.category.Category;
import taco.klkl.domain.category.domain.category.CategoryType;
import taco.klkl.domain.category.domain.subcategory.Subcategory;
import taco.klkl.domain.category.domain.subcategory.SubcategoryType;
import taco.klkl.domain.comment.domain.Comment;
import taco.klkl.domain.comment.dto.request.CommentCreateUpdateRequest;
import taco.klkl.domain.comment.dto.response.CommentResponse;
import taco.klkl.domain.comment.exception.CommentNotFoundException;
import taco.klkl.domain.comment.exception.CommentProductNotMatchException;
import taco.klkl.domain.comment.service.CommentServiceImpl;
import taco.klkl.domain.member.domain.Member;
import taco.klkl.domain.product.domain.Product;
import taco.klkl.domain.product.domain.Rating;
import taco.klkl.domain.product.exception.ProductNotFoundException;
import taco.klkl.domain.product.service.ProductService;
import taco.klkl.domain.region.domain.city.City;
import taco.klkl.domain.region.domain.city.CityType;
import taco.klkl.domain.region.domain.country.Country;
import taco.klkl.domain.region.domain.country.CountryType;
import taco.klkl.domain.region.domain.currency.Currency;
import taco.klkl.domain.region.domain.currency.CurrencyType;
import taco.klkl.domain.region.domain.region.Region;
import taco.klkl.domain.region.domain.region.RegionType;
import taco.klkl.global.error.exception.ErrorCode;

@WebMvcTest(CommentController.class)
public class CommentControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private CommentServiceImpl commentServiceImpl;

	@MockBean
	private ProductService productService;

	@Autowired
	private ObjectMapper objectMapper;

	private final Long productId = 1L;
	private final Long commentId = 1L;

	private final Member member = Member.of("name");

	private final Region region = Region.from(RegionType.SOUTHEAST_ASIA);

	private final Currency currency = Currency.of(
		CurrencyType.MALAYSIAN_RINGGIT
	);

	private final Country country = Country.of(
		CountryType.MALAYSIA,
		region,
		"image/malaysia-wallpaper.jpg",
		currency
	);

	private final City city = City.of(CityType.KUALA_LUMPUR, country);

	private final Category category = Category.of(CategoryType.FOOD);

	private final Subcategory subcategory = Subcategory.of(category, SubcategoryType.INSTANT_FOOD);

	private final Product product = Product.of(
		"name",
		"description",
		"address",
		1000,
		Rating.FIVE,
		member,
		city,
		subcategory,
		currency
	);

	private final Comment comment1 = Comment.of(product, member, "개추 ^^");
	private final Comment comment2 = Comment.of(product, member, "안녕하세요");

	private final CommentCreateUpdateRequest commentCreateRequestDto = new CommentCreateUpdateRequest(
		"개추 ^^"
	);

	private final CommentCreateUpdateRequest commentUpdateRequestDto = new CommentCreateUpdateRequest(
		"윤상정은 바보다, 반박시 님 말이 틀림."
	);

	@Test
	@DisplayName("상품에 있는 모든 댓글 반환 테스트")
	public void testGetComment() throws Exception {
		//given
		List<CommentResponse> responseDtos = Arrays.asList(CommentResponse.from(comment1),
			CommentResponse.from(comment2));

		when(commentServiceImpl.findCommentsByProductId(productId)).thenReturn(responseDtos);

		//when & then
		mockMvc.perform(get("/v1/products/{productId}/comments", productId)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.isSuccess", is(true)))
			.andExpect(jsonPath("$.data", hasSize(2)))
			.andExpect(jsonPath("$.data[0].id", is(comment1.getId())))
			.andExpect(jsonPath("$.data[0].content", is(comment1.getContent())))
			.andExpect(jsonPath("$.data[1].id", is(comment2.getId())))
			.andExpect(jsonPath("$.data[1].content", is(comment2.getContent())));

		verify(commentServiceImpl, times(1))
			.findCommentsByProductId(productId);
	}

	@Test
	@DisplayName("댓글 등록 성공 테스트")
	public void testCreateComment() throws Exception {
		//given
		CommentResponse responseDto = CommentResponse.from(comment1);

		when(commentServiceImpl.createComment(any(Long.class), any(CommentCreateUpdateRequest.class))).thenReturn(
			responseDto);

		//when & then
		mockMvc.perform(post("/v1/products/{productId}/comments", productId)
				.content(objectMapper.writeValueAsString(commentCreateRequestDto))
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.isSuccess", is(true)))
			.andExpect(jsonPath("$.data.id", is(comment1.getId())))
			.andExpect(jsonPath("$.data.user.id", is(comment1.getMember().getId())))
			.andExpect(jsonPath("$.data.content", is(comment1.getContent())));

		verify(commentServiceImpl, times(1))
			.createComment(productId, commentCreateRequestDto);
	}

	@Test
	@DisplayName("댓글 등록시 존재하지 않는 상품이라 실패하는 경우 테스트")
	public void testCreateCommentWhenProductNotFound() throws Exception {
		//given
		Long wrongProductId = 2L;

		doThrow(new ProductNotFoundException())
			.when(commentServiceImpl)
			.createComment(any(Long.class), any(CommentCreateUpdateRequest.class));

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
		///given
		CommentResponse responseDto = CommentResponse.from(comment1);

		when(commentServiceImpl.updateComment(
			any(Long.class),
			any(Long.class),
			any(CommentCreateUpdateRequest.class)))
			.thenReturn(responseDto);

		//when & then
		mockMvc.perform(put("/v1/products/{productId}/comments/{commentId}", productId, commentId)
				.content(objectMapper.writeValueAsString(commentUpdateRequestDto))
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.isSuccess", is(true)))
			.andExpect(jsonPath("$.data.id", is(comment1.getId())))
			.andExpect(jsonPath("$.data.user.id", is(comment1.getMember().getId())))
			.andExpect(jsonPath("$.data.content", is(comment1.getContent())));

		verify(commentServiceImpl, times(1))
			.updateComment(productId, commentId, commentUpdateRequestDto);
	}

	@Test
	@DisplayName("댓글 수정시 존재하지 않는 댓글이라 실패하는 경우 테스트")
	public void testUpdateCommentWhenCommentNotFound() throws Exception {
		///given
		Long wrongCommentId = 2L;

		when(commentServiceImpl.updateComment(any(Long.class), any(Long.class), any(CommentCreateUpdateRequest.class)))
			.thenThrow(new CommentNotFoundException());

		//when & then
		mockMvc.perform(put("/v1/products/{productId}/comments/{wrongCommentId}", productId, wrongCommentId)
				.content(objectMapper.writeValueAsString(commentUpdateRequestDto))
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.isSuccess", is(false)))
			.andExpect(jsonPath("$.status", is(ErrorCode.COMMENT_NOT_FOUND.getHttpStatus().value())))
			.andExpect(jsonPath("$.data.message", is(ErrorCode.COMMENT_NOT_FOUND.getMessage())));

		verify(commentServiceImpl, times(1))
			.updateComment(productId, wrongCommentId, commentUpdateRequestDto);
	}

	@Test
	@DisplayName("댓글 수정시 존재하지 않는 상품이라 실패하는 경우 테스트")
	public void testUpdateCommentWhenProductNotFound() throws Exception {
		//given
		Long wrongProductId = 2L;

		when(commentServiceImpl.updateComment(any(Long.class), any(Long.class), any(CommentCreateUpdateRequest.class)))
			.thenThrow(new ProductNotFoundException());

		//when & then
		mockMvc.perform(put("/v1/products/{wrongProductId}/comments/{commentId}", wrongProductId, commentId)
				.content(objectMapper.writeValueAsString(commentUpdateRequestDto))
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.isSuccess", is(false)))
			.andExpect(jsonPath("$.status", is(ErrorCode.PRODUCT_NOT_FOUND.getHttpStatus().value())))
			.andExpect(jsonPath("$.data.message", is(ErrorCode.PRODUCT_NOT_FOUND.getMessage())));

		verify(commentServiceImpl, times(1))
			.updateComment(wrongProductId, commentId, commentUpdateRequestDto);
	}

	@Test
	@DisplayName("댓글 수정시 존재하는 상품이지만 댓글에 저장된 상품 Id와 달라 실패하는 경우 테스트")
	public void testUpdateCommentWhenExistProductButNotMatchWithComment() throws Exception {
		//given

		when(commentServiceImpl.updateComment(any(Long.class), any(Long.class), any(CommentCreateUpdateRequest.class)))
			.thenThrow(new CommentProductNotMatchException());

		//when & then
		mockMvc.perform(put("/v1/products/{wrongProductId}/comments/{commentId}", productId, commentId)
				.content(objectMapper.writeValueAsString(commentUpdateRequestDto))
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.isSuccess", is(false)))
			.andExpect(jsonPath("$.status", is(ErrorCode.COMMENT_PRODUCT_NOT_MATCH.getHttpStatus().value())))
			.andExpect(jsonPath("$.data.message", is(ErrorCode.COMMENT_PRODUCT_NOT_MATCH.getMessage())));

		verify(commentServiceImpl, times(1))
			.updateComment(productId, commentId, commentUpdateRequestDto);
	}

	@Test
	@DisplayName("댓글 삭제 성공 테스트")
	public void testDeleteComment() throws Exception {
		//given

		//when & then
		mockMvc.perform(delete("/v1/products/{productId}/comments/{commentId}", productId, commentId))
			.andExpect(status().isNoContent())
			.andExpect(jsonPath("$.isSuccess", is(true)))
			.andExpect(jsonPath("$.data", nullValue()));

		verify(commentServiceImpl, times(1)).deleteComment(productId, commentId);
	}

	@Test
	@DisplayName("댓글 삭제시 존재하지 않는 댓글이라 실패하는 경우 테스트")
	public void testDeleteCommentWhenCommentNotFound() throws Exception {
		//given
		Long wrongCommentId = 2L;

		doThrow(new CommentNotFoundException()).when(commentServiceImpl).deleteComment(productId, wrongCommentId);

		//when & then
		mockMvc.perform(delete("/v1/products/{productId}/comments/{wrongCommentId}", productId, wrongCommentId)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.isSuccess", is(false)))
			.andExpect(jsonPath("$.status", is(ErrorCode.COMMENT_NOT_FOUND.getHttpStatus().value())))
			.andExpect(jsonPath("$.data.message", is(ErrorCode.COMMENT_NOT_FOUND.getMessage())));

		verify(commentServiceImpl, times(1)).deleteComment(productId, wrongCommentId);
	}

	@Test
	@DisplayName("댓글 삭제시 존재하지 않는 상품이라 실패하는 경우 테스트")
	public void testDeleteCommentWhenProductNotFound() throws Exception {
		//given
		Long wrongProductId = 2L;

		doThrow(new ProductNotFoundException()).when(commentServiceImpl).deleteComment(wrongProductId, commentId);

		//when & then
		mockMvc.perform(delete("/v1/products/{wrongProductId}/comments/{commentId}", wrongProductId, commentId)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.isSuccess", is(false)))
			.andExpect(jsonPath("$.status", is(ErrorCode.PRODUCT_NOT_FOUND.getHttpStatus().value())))
			.andExpect(jsonPath("$.data.message", is(ErrorCode.PRODUCT_NOT_FOUND.getMessage())));

		verify(commentServiceImpl, times(1)).deleteComment(wrongProductId, commentId);
	}

	@Test
	@DisplayName("댓글 삭제시 존재하는 상품이지만 댓글에 저장된 상품 Id와 달라 실패하는 경우 테스트")
	public void testDeleteCommentWhenExistProductButNotMatchWithComment() throws Exception {
		//given

		doThrow(new CommentProductNotMatchException()).when(commentServiceImpl).deleteComment(productId, commentId);

		//when & then
		mockMvc.perform(delete("/v1/products/{wrongProductId}/comments/{commentId}", productId, commentId)
				.content(objectMapper.writeValueAsString(commentUpdateRequestDto))
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.isSuccess", is(false)))
			.andExpect(jsonPath("$.status", is(ErrorCode.COMMENT_PRODUCT_NOT_MATCH.getHttpStatus().value())))
			.andExpect(jsonPath("$.data.message", is(ErrorCode.COMMENT_PRODUCT_NOT_MATCH.getMessage())));

		verify(commentServiceImpl, times(1))
			.deleteComment(productId, commentId);
	}
}
