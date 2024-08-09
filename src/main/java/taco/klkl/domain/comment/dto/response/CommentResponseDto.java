package taco.klkl.domain.comment.dto.response;

import java.time.LocalDateTime;

import taco.klkl.domain.comment.domain.Comment;

public record CommentResponseDto(
	Long commentId,
	Long userId,
	String content,
	LocalDateTime createdAt
) {
	public static CommentResponseDto from(final Comment comment) {
		return new CommentResponseDto(comment.getId(),
			comment.getUser().getId(),
			comment.getContent(),
			comment.getCreatedAt()
		);
	}
}
