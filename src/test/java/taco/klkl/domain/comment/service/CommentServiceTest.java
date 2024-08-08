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
import taco.klkl.domain.comment.dto.request.CommentRequestDto;
import taco.klkl.domain.comment.dto.response.CommentResponseDto;
import taco.klkl.domain.comment.exception.CommentNotFoundException;
import taco.klkl.domain.product.exception.ProductNotFoundException;
import taco.klkl.domain.user.domain.User;
import taco.klkl.domain.user.dto.request.UserCreateRequestDto;
import taco.klkl.global.common.enums.Gender;
import taco.klkl.global.util.UserUtil;

@ExtendWith(MockitoExtension.class)
@Transactional
public class CommentServiceTest {
	@Mock
	private CommentRepository commentRepository;

	@Mock
	private UserUtil userUtil;

	@InjectMocks
	private CommentService commentService;

	private final UserCreateRequestDto userRequestDto = new UserCreateRequestDto(
		"이상화",
		"남",
		19,
		"image/ideal-flower.jpg",
		"저는 이상화입니다."
	);

	private final User user = User.of(
		userRequestDto.profile(),
		userRequestDto.name(),
		Gender.getGenderByDescription(userRequestDto.description()),
		userRequestDto.age(),
		userRequestDto.description()
	);

	private final CommentRequestDto commentCreateRequestDto = new CommentRequestDto(
		1L,
		"이거 진짜에요?"
	);

	private final CommentRequestDto commentUpdateRequestDto = new CommentRequestDto(
		1L,
		"윤상정은 바보다, 반박시 님 말이 틀림."
	);

	@Test
	@DisplayName("상품에 있는 모든 댓글 반환 테스트")
	public void testGetComments() {
		//given
		Long productId = 1L;
		Comment comment1 = Comment.of(1L, user, "쎄마 네이이이이이암 마");
		Comment comment2 = Comment.of(2L, user, "이거 진짜에요?");
		List<Comment> comments = List.of(comment1, comment2);

		when(commentRepository.findAllByProductId(productId)).thenReturn(comments);

		//when
		List<CommentResponseDto> result = commentService.getComments(productId);

		//then
		assertThat(result.get(0).commentId()).isEqualTo(comment1.getId());
		assertThat(result.get(1).commentId()).isEqualTo(comment2.getId());
		assertThat(result.get(0).content()).isEqualTo(comment1.getContent());
		assertThat(result.get(1).content()).isEqualTo(comment2.getContent());
		assertThat(result.get(0).createdAt()).isEqualTo(comment1.getDate());
		assertThat(result.get(1).createdAt()).isEqualTo(comment2.getDate());

		verify(commentRepository, times(1)).findAllByProductId(productId);
	}

	@Test
	@DisplayName("댓글 등록이 성공하는 경우 테스트")
	public void testCreateComment() {
		//given
		Long productId = 1L;
		Comment comment = Comment.of(1L, user, "이거 진짜에요?");

		when(userUtil.findTestUser()).thenReturn(user);
		when(commentRepository.save(any(Comment.class))).thenReturn(comment);

		//when
		CommentResponseDto result = commentService.createComment(productId, commentCreateRequestDto);

		//then
		assertThat(result.commentId()).isEqualTo(comment.getId());
		assertThat(result.userId()).isEqualTo(user.getId());
		assertThat(result.content()).isEqualTo(comment.getContent());
		assertThat(result.createdAt()).isEqualTo(comment.getDate());
	}

	@Test
	@DisplayName("댓글 수정이 성공하는 경우 테스트")
	public void testUpdateComment() {
		Long productId = 1L;
		Long commentID = 1L;

		Comment comment = Comment.of(1L, user, "이거 진짜에요?");

		when(commentRepository.findById(commentID)).thenReturn(Optional.of(comment));

		//when
		CommentResponseDto result = commentService.updateComment(productId, commentID, commentUpdateRequestDto);

		//then
		assertThat(result.commentId()).isEqualTo(comment.getId());
		assertThat(result.userId()).isEqualTo(user.getId());
		assertThat(result.content()).isEqualTo(commentUpdateRequestDto.content());
		assertThat(result.createdAt()).isEqualTo(comment.getDate());
	}

	@Test
	@DisplayName("댓글 수정시 존재하지 않는 댓글이라 실패하는 경우 테스트")
	public void testUpdateCommentWhenCommentNotFound() {
		Long productId = 1L;
		Long commentID = 1L;

		Comment comment = Comment.of(1L, user, "이거 진짜에요?");

		when(commentRepository.findById(commentID)).thenReturn(Optional.empty());

		//when & then
		assertThrows(CommentNotFoundException.class, () -> commentService.deleteComment(productId, commentID));
		verify(commentRepository, never()).save(any(Comment.class));
	}

	@Test
	@DisplayName("댓글 수정시 존재하지 않는 상품이라 실패하는 경우 테스트")
	public void testUpdateCommentWhenProductNotFound() {
		Long productId = 1L;
		Long commentID = 1L;

		Comment comment = Comment.of(2L, user, "이거 진짜에요?");

		when(commentRepository.findById(commentID)).thenReturn(Optional.of(comment));

		//when & then
		assertThrows(ProductNotFoundException.class, () -> commentService.deleteComment(productId, commentID));
		verify(commentRepository, never()).save(any(Comment.class));
	}

	@Test
	@DisplayName("댓글 삭제가 성공하는 경우 테스트")
	public void testDeleteComment() {
		Long productId = 1L;
		Long commentID = 1L;

		//given
		Comment comment = Comment.of(1L, user, "이거 진짜에요?");

		when(commentRepository.findById(commentID)).thenReturn(Optional.of(comment));

		commentService.deleteComment(productId, commentID);

		verify(commentRepository).findById(1L);
		verify(commentRepository).delete(comment);
	}

	@Test
	@DisplayName("댓글 삭제시 존재하지 않는 댓글이라 실패하는 경우 테스트")
	public void testDeleteCommentWhenCommentNotFound() {
		Long productId = 1L;
		Long commentID = 1L;

		Comment comment = Comment.of(1L, user, "이거 진짜에요?");

		when(commentRepository.findById(commentID)).thenReturn(Optional.empty());

		//when & then
		assertThrows(CommentNotFoundException.class, () -> commentService.deleteComment(productId, commentID));
		verify(commentRepository, never()).delete(any(Comment.class));
	}

	@Test
	@DisplayName("댓글 삭제시 존재하지 않는 상품이라 실패하는 경우 테스트")
	public void testDeleteCommentWhenProductNotFound() {
		Long productId = 1L;
		Long commentID = 1L;

		Comment comment = Comment.of(2L, user, "이거 진짜에요?");

		when(commentRepository.findById(commentID)).thenReturn(Optional.of(comment));

		//when & then
		assertThrows(ProductNotFoundException.class, () -> commentService.deleteComment(productId, commentID));
		verify(commentRepository, never()).delete(any(Comment.class));
	}
}
