package taco.klkl.domain.comment.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import taco.klkl.domain.comment.dao.CommentRepository;
import taco.klkl.domain.comment.domain.Comment;
import taco.klkl.domain.comment.dto.request.CommentCreateUpdateRequest;
import taco.klkl.domain.comment.dto.response.CommentResponse;
import taco.klkl.domain.comment.exception.CommentNotFoundException;
import taco.klkl.domain.comment.exception.CommentProductNotMatch;
import taco.klkl.domain.notification.service.NotificationService;
import taco.klkl.domain.product.domain.Product;
import taco.klkl.domain.product.exception.ProductNotFoundException;
import taco.klkl.domain.user.domain.User;
import taco.klkl.global.util.ProductUtil;
import taco.klkl.global.util.UserUtil;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {
	private final CommentRepository commentRepository;

	private final NotificationService notificationService;

	private final UserUtil userUtil;
	private final ProductUtil productUtil;

	public List<CommentResponse> getComments(final Long productId) {
		validateProductId(productId);
		final List<Comment> comments = commentRepository.findAllByProduct_Id(productId);
		return comments.stream()
			.map(CommentResponse::from)
			.toList();
	}

	@Transactional
	public CommentResponse createComment(
		final Long productId,
		final CommentCreateUpdateRequest commentCreateRequestDto
	) {
		final Comment comment = createCommentEntity(productId, commentCreateRequestDto);
		commentRepository.save(comment);
		notificationService.createNotification(comment);
		return CommentResponse.from(comment);
	}

	@Transactional
	public CommentResponse updateComment(
		final Long productId,
		final Long commentId,
		final CommentCreateUpdateRequest commentUpdateRequestDto
	) {
		validateProductId(productId);
		final Comment comment = commentRepository.findById(commentId)
			.orElseThrow(CommentNotFoundException::new);
		validateSameProductId(comment, productId);
		updateCommentEntity(comment, commentUpdateRequestDto);

		return CommentResponse.from(comment);
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
		final CommentCreateUpdateRequest commentCreateUpdateRequest
	) {
		//TODO: getCurrentUser() 함수로 교채
		final User user = userUtil.findTestUser();
		final Product product = productUtil.getProductEntityById(productId);
		return Comment.of(
			product,
			user,
			commentCreateUpdateRequest.content()
		);
	}

	private void updateCommentEntity(
		final Comment comment,
		final CommentCreateUpdateRequest commentCreateUpdateRequest
	) {
		comment.update(commentCreateUpdateRequest.content());
	}

	private void validateProductId(final Long productId) {
		boolean isValidProductId = productUtil.existsProductById(productId);
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
