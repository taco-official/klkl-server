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
import taco.klkl.domain.comment.dto.request.CommentCreateUpdateRequestDto;
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
	@Operation(summary = "댓글 목록 조회", description = "상품 목록에 대한 댓글 목록을 반환합니다.")
	public List<CommentResponseDto> getComments(@PathVariable final Long productId) {
		final List<CommentResponseDto> commentList = commentService.getComments(productId);
		return commentList;
	}

	@PostMapping
	@Operation(summary = "댓글 등록", description = "작성한 댓글을 저장합니다.")
	@ResponseStatus(HttpStatus.CREATED)
	public CommentResponseDto addComment(
		@PathVariable final Long productId,
		@RequestBody @Valid final CommentCreateUpdateRequestDto commentCreateRequestDto
	) {
		final CommentResponseDto commentResponseDto = commentService.createComment(
			productId,
			commentCreateRequestDto
		);
		return commentResponseDto;
	}

	@PutMapping("/{commentId}")
	@Operation(summary = "댓글 수정", description = "작성한 댓글을 수정합니다.")
	@ResponseStatus(HttpStatus.OK)
	public CommentResponseDto updateComment(
		@PathVariable final Long productId,
		@PathVariable final Long commentId,
		@RequestBody @Valid CommentCreateUpdateRequestDto commentUpdateRequestDto
	) {
		final CommentResponseDto commentResponseDto = commentService.updateComment(
			productId,
			commentId,
			commentUpdateRequestDto
		);
		return commentResponseDto;
	}

	@DeleteMapping("/{commentId}")
	@Operation(summary = "댓글 삭제", description = "작성한 댓글을 삭제합니다.")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<Void> deleteComment(
		@PathVariable final Long productId,
		@PathVariable final Long commentId
	) {
		commentService.deleteComment(productId, commentId);
		return ResponseEntity.noContent().build();
	}
}
