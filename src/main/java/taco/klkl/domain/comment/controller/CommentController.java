package taco.klkl.domain.comment.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import taco.klkl.domain.comment.dto.request.CommentRequestDto;
import taco.klkl.domain.comment.dto.response.CommentResponseDto;
import taco.klkl.domain.comment.service.CommentService;
import taco.klkl.domain.product.service.ProductService;

@RestController
@RequestMapping("/v1/products/{productId}/comments")
@Tag(name = "4. 댓글", description = "댓글 관련 API")
@RequiredArgsConstructor
public class CommentController {
	private final CommentService commentService;
	private final ProductService productService;

	@GetMapping
	@Operation(description = "상품 목록에 대한 댓글 목록을 반환합니다.")
	public List<CommentResponseDto> getComments(@PathVariable Long productId) {
		productService.isProductExits(productId);
		List<CommentResponseDto> commentList = commentService.getComments(productId);
		return commentList;
	}

	@PostMapping
	@Operation(description = "작성한 댓글을 저장합니다.")
	@ResponseStatus(HttpStatus.CREATED)
	public CommentResponseDto addComments(
		@PathVariable Long productId,
		@RequestBody @Valid CommentRequestDto commentCreateRequestDto
	) {
		productService.isProductExits(productId);
		CommentResponseDto commentResponseDto = commentService.createComment(productId, commentCreateRequestDto);
		return commentResponseDto;
	}

	@PutMapping("/{commentId}")
	@Operation(description = "작성한 댓글을 수정합니다.")
	@ResponseStatus(HttpStatus.OK)
	public CommentResponseDto updateComments(
		@PathVariable Long productId,
		@PathVariable Long commentId,
		@RequestBody @Valid CommentRequestDto commentUpdateRequestDto
	) {
		CommentResponseDto commentResponseDto = commentService.updateComment(
			productId,
			commentId,
			commentUpdateRequestDto);
		return commentResponseDto;
	}

	@DeleteMapping("/{commentId}")
	@Operation(description = "작성한 댓글을 삭제합니다.")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<Void> deleteComments(@PathVariable Long productId, @PathVariable Long commentId) {
		commentService.deleteComment(productId, commentId);
		return ResponseEntity.noContent().build();
	}
}
