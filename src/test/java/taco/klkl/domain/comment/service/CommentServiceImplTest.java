package taco.klkl.domain.comment.service;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import taco.klkl.domain.comment.dao.CommentRepository;
import taco.klkl.domain.comment.domain.Comment;
import taco.klkl.domain.comment.dto.request.CommentCreateUpdateRequest;
import taco.klkl.domain.comment.dto.response.CommentResponse;
import taco.klkl.domain.comment.exception.CommentNotFoundException;
import taco.klkl.domain.comment.exception.CommentProductNotMatchException;
import taco.klkl.domain.member.domain.Member;
import taco.klkl.domain.notification.service.NotificationService;
import taco.klkl.domain.product.domain.Product;
import taco.klkl.domain.product.exception.ProductNotFoundException;
import taco.klkl.global.util.MemberUtil;
import taco.klkl.global.util.ProductUtil;

@ExtendWith(MockitoExtension.class)
@Transactional
public class CommentServiceImplTest {
	@Mock
	private CommentRepository commentRepository;

	@Mock
	private MemberUtil memberUtil;

	@Mock
	private ProductUtil productUtil;

	@Mock
	private NotificationService notificationService;

	@InjectMocks
	private CommentServiceImpl commentServiceImpl;

	private final Member member = Member.of("name");

	private final Product product = mock(Product.class);

	private final CommentCreateUpdateRequest commentCreateRequestDto = new CommentCreateUpdateRequest(
		"이거 진짜에요?"
	);

	private final CommentCreateUpdateRequest commentUpdateRequestDto = new CommentCreateUpdateRequest(
		"윤상정은 바보다, 반박시 님 말이 틀림."
	);

	@Test
	@DisplayName("상품에 있는 모든 댓글 반환 테스트")
	public void testFindCommentsByProductId() {
		//given
		Long productId = 1L;
		Comment comment1 = Comment.of(product, member, "쎄마 네이이이이이암 마");
		Comment comment2 = Comment.of(product, member, "이거 진짜에요?");
		List<Comment> comments = List.of(comment1, comment2);

		when(product.getId()).thenReturn(productId);
		when(commentRepository.findByProductIdOrderByCreatedAtDesc(productId)).thenReturn(comments);

		//when
		List<CommentResponse> result = commentServiceImpl.findCommentsByProductId(productId);

		//then
		assertThat(result.get(0).id()).isEqualTo(comment1.getId());
		assertThat(result.get(1).id()).isEqualTo(comment2.getId());
		assertThat(result.get(0).content()).isEqualTo(comment1.getContent());
		assertThat(result.get(1).content()).isEqualTo(comment2.getContent());
		assertThat(result.get(0).createdAt()).isEqualTo(comment1.getCreatedAt());
		assertThat(result.get(1).createdAt()).isEqualTo(comment2.getCreatedAt());

		verify(commentRepository, times(1)).findByProductIdOrderByCreatedAtDesc(productId);
	}

	@Test
	@DisplayName("댓글 등록이 성공하는 경우 테스트")
	public void testCreateComment() {
		//given
		final Long productId = 1L;
		final Comment comment = Comment.of(product, member, "이거 진짜에요?");

		when(memberUtil.getCurrentMember()).thenReturn(member);
		when(commentRepository.save(any(Comment.class))).thenReturn(comment);

		//when
		CommentResponse result = commentServiceImpl.createComment(productId, commentCreateRequestDto);

		//then
		assertThat(result.id()).isEqualTo(comment.getId());
		assertThat(result.member().id()).isEqualTo(member.getId());
		assertThat(result.content()).isEqualTo(comment.getContent());
	}

	@Test
	@DisplayName("댓글 수정이 성공하는 경우 테스트")
	public void testUpdateComment() {
		//
		Long productId = 1L;
		Long commentId = 1L;

		Comment comment = Comment.of(product, member, "이거 진짜에요?");

		when(memberUtil.getCurrentMember()).thenReturn(member);
		when(product.getId()).thenReturn(productId);
		when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

		//when
		CommentResponse result = commentServiceImpl.updateComment(productId, commentId, commentUpdateRequestDto);

		//then
		assertThat(result.id()).isEqualTo(comment.getId());
		assertThat(result.member().id()).isEqualTo(member.getId());
		assertThat(result.content()).isEqualTo(commentUpdateRequestDto.content());
		assertThat(result.createdAt()).isEqualTo(comment.getCreatedAt());
	}

	@Test
	@DisplayName("댓글 수정시 존재하지 않는 댓글이라 실패하는 경우 테스트")
	public void testUpdateCommentWhenCommentNotFound() {
		//given
		Long productId = 1L;
		Long commentId = 1L;

		Comment comment = Comment.of(product, member, "이거 진짜에요?");

		when(product.getId()).thenReturn(productId);

		//when & then
		assertThrows(CommentNotFoundException.class, () -> commentServiceImpl.deleteComment(productId, commentId));
		verify(commentRepository, never()).save(any(Comment.class));
	}

	@Test
	@DisplayName("댓글 수정시 존재하지 않는 상품이라 실패하는 경우 테스트")
	public void testUpdateCommentWhenProductNotFound() {
		// given
		Long productId = 1L;
		Long commentId = 1L;

		when(product.getId()).thenReturn(productId);
		doThrow(ProductNotFoundException.class).when(productUtil).validateProductId(productId);

		//when & then
		assertThrows(ProductNotFoundException.class,
			() -> commentServiceImpl.updateComment(productId, commentId, commentUpdateRequestDto));
		verify(commentRepository, never()).save(any(Comment.class));
	}

	@Test
	@DisplayName("댓글 수정시 존재하는 상품이지만 댓글에 저장된 상품 Id와 달라 실패하는 경우 테스트")
	public void testUpdateCommentWhenExistProductButNotMatchWithComment() {
		// given
		Long productId = 1L;
		Long wrongProductId = 2L;
		Long commentId = 1L;
		Comment comment = Comment.of(product, member, "이거 진짜에요?");

		when(memberUtil.getCurrentMember()).thenReturn(member);
		when(product.getId()).thenReturn(wrongProductId);
		when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

		//when & then
		assertThrows(CommentProductNotMatchException.class,
			() -> commentServiceImpl.updateComment(productId, commentId, commentUpdateRequestDto));
		verify(commentRepository, never()).save(any(Comment.class));
	}

	@Test
	@DisplayName("댓글 삭제가 성공하는 경우 테스트")
	public void testDeleteComment() {
		//given
		Long productId = 1L;
		Long commentId = 1L;

		Comment comment = Comment.of(product, member, "이거 진짜에요?");

		when(memberUtil.getCurrentMember()).thenReturn(member);
		when(product.getId()).thenReturn(productId);
		when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

		commentServiceImpl.deleteComment(productId, commentId);

		verify(commentRepository).findById(commentId);
		verify(commentRepository).delete(comment);
	}

	@Test
	@DisplayName("댓글 삭제시 존재하지 않는 댓글이라 실패하는 경우 테스트")
	public void testDeleteCommentWhenCommentNotFound() {
		Long productId = 1L;
		Long commentID = 1L;

		Comment comment = Comment.of(product, member, "이거 진짜에요?");

		when(product.getId()).thenReturn(productId);
		when(commentRepository.findById(commentID)).thenReturn(Optional.empty());

		//when & then
		assertThrows(CommentNotFoundException.class, () -> commentServiceImpl.deleteComment(productId, commentID));
		verify(commentRepository, never()).delete(any(Comment.class));
	}

	@Test
	@DisplayName("댓글 삭제시 존재하지 않는 상품이라 실패하는 경우 테스트")
	public void testDeleteCommentWhenProductNotFound() {
		Long productId = 1L;
		Long commentID = 1L;

		doThrow(ProductNotFoundException.class).when(productUtil).validateProductId(productId);

		//when & then
		assertThrows(ProductNotFoundException.class, () -> commentServiceImpl.deleteComment(productId, commentID));
		verify(commentRepository, never()).delete(any(Comment.class));
	}

	@Test
	@DisplayName("댓글 삭제시 존재하는 상품이지만 댓글에 저장된 상품 Id와 달라 실패하는 경우 테스트")
	public void testDeleteCommentWhenExistProductButNotMatchWithComment() {
		// given
		Long productId = 1L;
		Long wrongProductId = 2L;
		Long commentId = 1L;
		Comment comment = Comment.of(product, member, "이거 진짜에요?");

		when(memberUtil.getCurrentMember()).thenReturn(member);
		when(product.getId()).thenReturn(wrongProductId);
		when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

		//when & then
		assertThrows(CommentProductNotMatchException.class,
			() -> commentServiceImpl.deleteComment(productId, commentId));
		verify(commentRepository, never()).delete(any(Comment.class));
	}
}
