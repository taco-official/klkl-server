package taco.klkl.domain.comment.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import taco.klkl.domain.comment.dao.CommentRepository;
import taco.klkl.domain.comment.domain.Comment;
import taco.klkl.domain.comment.dto.request.CommentCreateUpdateRequestDto;
import taco.klkl.domain.comment.dto.response.CommentResponseDto;
import taco.klkl.domain.comment.exception.CommentNotFoundException;
import taco.klkl.domain.product.exception.ProductNotFoundException;
import taco.klkl.domain.user.domain.User;
import taco.klkl.global.util.UserUtil;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {
	private final CommentRepository commentRepository;
	private final UserUtil userUtil;

	public List<CommentResponseDto> getComments(final Long productId) {
		final List<Comment> comments = commentRepository.findAllByProductId(productId);
		return comments.stream()
			.map(CommentResponseDto::from)
			.toList();
	}

	@Transactional
	public CommentResponseDto createComment(
		final Long productId,
		final CommentCreateUpdateRequestDto commentCreateRequestDto
	) {
		final Comment comment = createCommentEntity(productId, commentCreateRequestDto);
		commentRepository.save(comment);
		return CommentResponseDto.from(comment);
	}

	@Transactional
	public CommentResponseDto updateComment(
		final Long productId,
		final Long commentId,
		final CommentCreateUpdateRequestDto commentUpdateRequestDto
	) {
		final Comment comment = commentRepository.findById(commentId)
			.orElseThrow(CommentNotFoundException::new);
		Optional.of(comment.getProductId())
			.filter(id -> id.equals(productId))
			.orElseThrow(ProductNotFoundException::new);
		comment.update(commentUpdateRequestDto);
		return CommentResponseDto.from(comment);
	}

	@Transactional
	public void deleteComment(
		final Long productId,
		final Long commentId
	) {
		final Comment comment = commentRepository.findById(commentId)
			.orElseThrow(CommentNotFoundException::new);
		Optional.of(comment.getProductId())
			.filter(id -> id.equals(productId))
			.orElseThrow(ProductNotFoundException::new);
		commentRepository.delete(comment);
	}

	private Comment createCommentEntity(
		final Long productId,
		final CommentCreateUpdateRequestDto commentCreateUpdateRequestDto
	) {
		//ToDo: getCurrentUser() 함수로 교채
		final User user = userUtil.findTestUser();
		return Comment.of(
			productId,
			user,
			commentCreateUpdateRequestDto.content()
		);
	}
}
