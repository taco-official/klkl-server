package taco.klkl.domain.comment.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import taco.klkl.domain.comment.dao.CommentRepository;
import taco.klkl.domain.comment.domain.Comment;
import taco.klkl.domain.comment.dto.request.CommentCreateUpdateRequestDto;
import taco.klkl.domain.comment.dto.response.CommentResponseDto;
import taco.klkl.domain.comment.exception.CommentNotFoundException;
import taco.klkl.domain.comment.exception.CommentProductNotMatch;
import taco.klkl.domain.product.domain.Product;
import taco.klkl.domain.product.exception.ProductNotFoundException;
import taco.klkl.domain.product.service.ProductService;
import taco.klkl.domain.user.domain.User;
import taco.klkl.global.util.UserUtil;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {
	private final CommentRepository commentRepository;
	private final ProductService productService;
	private final UserUtil userUtil;

	public List<CommentResponseDto> getComments(final Long productId) {
		validateProductId(productId);
		final List<Comment> comments = commentRepository.findAllByProduct_Id(productId);
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
		validateProductId(productId);
		final Comment comment = commentRepository.findById(commentId)
			.orElseThrow(CommentNotFoundException::new);
		validateSameProductId(comment, productId);
		updateCommentEntity(comment, commentUpdateRequestDto);

		return CommentResponseDto.from(comment);
	}

	@Transactional
	public void deleteComment(
		final Long productId,
		final Long commentId
	) {
		validateProductId(productId);
		final Comment comment = commentRepository.findById(commentId)
			.orElseThrow(CommentNotFoundException::new);
		validateSameProductId(comment, productId);
		commentRepository.delete(comment);
	}

	private Comment createCommentEntity(
		final Long productId,
		final CommentCreateUpdateRequestDto commentCreateUpdateRequestDto
	) {
		//TODO: getCurrentUser() 함수로 교채
		final User user = userUtil.findTestUser();
		final Product product = productService.getProductEntityById(productId);
		return Comment.of(
			product,
			user,
			commentCreateUpdateRequestDto.content()
		);
	}

	private void updateCommentEntity(
		final Comment comment,
		final CommentCreateUpdateRequestDto commentCreateUpdateRequestDto
	) {
		comment.update(commentCreateUpdateRequestDto.content());
	}

	private void validateProductId(final Long productId) {
		boolean isValidProductId = productService.existsProductById(productId);
		if (!isValidProductId) {
			throw new ProductNotFoundException();
		}
	}

	private void validateSameProductId(final Comment comment, final Long productId) {
		final Long commentProductId = comment.getProduct().getId();
		if (!commentProductId.equals(productId)) {
			throw new CommentProductNotMatch();
		}
	}
}
