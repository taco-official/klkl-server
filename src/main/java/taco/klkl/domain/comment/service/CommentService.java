package taco.klkl.domain.comment.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import taco.klkl.domain.comment.dao.CommentRepository;
import taco.klkl.domain.comment.domain.Comment;
import taco.klkl.domain.comment.dto.request.CommentRequestDto;
import taco.klkl.domain.comment.dto.response.CommentResponseDto;
import taco.klkl.domain.comment.exception.CommentNotFoundException;
import taco.klkl.domain.comment.exception.ProductNotMatchException;
import taco.klkl.domain.user.domain.User;
import taco.klkl.global.util.UserUtil;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {
	private final CommentRepository commentRepository;
	private final UserUtil userUtil;

	public List<CommentResponseDto> getComments(Long productId) {
		List<Comment> comments = commentRepository.findAllByProductId(productId);
		return comments.stream()
			.map(CommentResponseDto::from)
			.toList();
	}

	@Transactional
	public CommentResponseDto createComment(Long productId, CommentRequestDto commentCreateRequestDto) {
		Comment comment = createCommentEntity(productId, commentCreateRequestDto);
		commentRepository.save(comment);
		return CommentResponseDto.from(comment);
	}

	@Transactional
	public CommentResponseDto updateComment(Long productId, Long commentId, CommentRequestDto commentUpdateRequestDto) {
		Comment comment = commentRepository.findById(commentId)
			.orElseThrow(CommentNotFoundException::new);
		comment.update(comment.getUser(), commentUpdateRequestDto);
		Optional.of(comment.getProductId())
			.filter(id -> id.equals(productId))
			.orElseThrow(ProductNotMatchException::new);
		return CommentResponseDto.from(comment);
	}

	@Transactional
	public void deleteComment(Long productId, Long commentId) {
		Comment comment = commentRepository.findById(commentId)
			.orElseThrow(CommentNotFoundException::new);
		Optional.of(comment.getProductId())
			.filter(id -> id.equals(productId))
			.orElseThrow(ProductNotMatchException::new);
		commentRepository.delete(comment);
	}

	private Comment createCommentEntity(Long productId, CommentRequestDto commentRequestDto) {
		final User user = userUtil.findTestUser();
		return Comment.of(
			productId,
			user,
			commentRequestDto.content()
		);
	}
}
