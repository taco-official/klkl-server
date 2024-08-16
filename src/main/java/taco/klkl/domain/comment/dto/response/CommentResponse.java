package taco.klkl.domain.comment.dto.response;

import java.time.LocalDateTime;

import taco.klkl.domain.comment.domain.Comment;
import taco.klkl.domain.user.dto.response.UserSimpleResponseDto;

public record CommentResponse(
	Long commentId,
	UserSimpleResponseDto user,
	String content,
	LocalDateTime createdAt
) {
	public static CommentResponse from(final Comment comment) {
		return new CommentResponse(comment.getId(),
			UserSimpleResponseDto.from(comment.getUser()),
			comment.getContent(),
			comment.getCreatedAt()
		);
	}
}
