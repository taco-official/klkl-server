package taco.klkl.domain.comment.service;

import java.util.List;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import taco.klkl.domain.comment.dao.CommentRepository;
import taco.klkl.domain.comment.domain.Comment;
import taco.klkl.domain.comment.dto.request.CommentCreateUpdateRequest;
import taco.klkl.domain.comment.dto.response.CommentResponse;
import taco.klkl.domain.comment.exception.CommentMemberNotMatchException;
import taco.klkl.domain.comment.exception.CommentNotFoundException;
import taco.klkl.domain.comment.exception.CommentProductNotMatchException;
import taco.klkl.domain.member.domain.Member;
import taco.klkl.domain.notification.service.NotificationService;
import taco.klkl.domain.product.domain.Product;
import taco.klkl.global.util.MemberUtil;
import taco.klkl.global.util.ProductUtil;

@Slf4j
@Primary
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

	private final CommentRepository commentRepository;

	private final NotificationService notificationService;

	private final MemberUtil memberUtil;
	private final ProductUtil productUtil;

	public List<CommentResponse> findCommentsByProductId(final Long productId) {
		validateProductId(productId);
		final List<Comment> comments = commentRepository.findByProductIdOrderByCreatedAtDesc(productId);
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
		if (!isMyProduct(productId)) {
			notificationService.createNotificationByComment(comment);
		}
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
		validateMyComment(comment);
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
		validateMyComment(comment);
		validateSameProductId(comment, productId);
		commentRepository.delete(comment);
	}

	private Comment createCommentEntity(
		final Long productId,
		final CommentCreateUpdateRequest commentCreateUpdateRequest
	) {
		final Member member = memberUtil.getCurrentMember();
		final Product product = productUtil.findProductEntityById(productId);
		return Comment.of(
			product,
			member,
			commentCreateUpdateRequest.content()
		);
	}

	private boolean isMyProduct(final Long productId) {
		final Member me = memberUtil.getCurrentMember();
		final Product product = productUtil.findProductEntityById(productId);
		return product.getMember().equals(me);
	}

	private void updateCommentEntity(
		final Comment comment,
		final CommentCreateUpdateRequest commentCreateUpdateRequest
	) {
		comment.update(commentCreateUpdateRequest.content());
	}

	private void validateProductId(final Long productId) {
		productUtil.validateProductId(productId);
	}

	private void validateMyComment(final Comment comment) {
		final Member me = memberUtil.getCurrentMember();
		if (!comment.getMember().equals(me)) {
			throw new CommentMemberNotMatchException();
		}
	}

	private void validateSameProductId(final Comment comment, final Long productId) {
		final Long commentProductId = comment.getProduct().getId();
		if (!commentProductId.equals(productId)) {
			throw new CommentProductNotMatchException();
		}
	}
}
