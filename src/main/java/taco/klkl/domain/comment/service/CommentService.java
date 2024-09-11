package taco.klkl.domain.comment.service;

import java.util.List;

import org.springframework.stereotype.Service;

import taco.klkl.domain.comment.dto.request.CommentCreateUpdateRequest;
import taco.klkl.domain.comment.dto.response.CommentResponse;

@Service
public interface CommentService {

	List<CommentResponse> findCommentsByProductId(final Long productId);

	CommentResponse createComment(
		final Long productId,
		final CommentCreateUpdateRequest commentCreateRequestDto
	);

	CommentResponse updateComment(
		final Long productId,
		final Long commentId,
		final CommentCreateUpdateRequest commentUpdateRequestDto
	);

	void deleteComment(
		final Long productId,
		final Long commentId
	);
}
