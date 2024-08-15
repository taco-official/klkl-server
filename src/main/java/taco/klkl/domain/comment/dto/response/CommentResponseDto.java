package taco.klkl.domain.comment.dto.response;

import java.time.LocalDateTime;

import taco.klkl.domain.comment.domain.Comment;
import taco.klkl.domain.user.dto.response.UserSimpleResponseDto;

public record CommentResponseDto(
	Long commentId,
	UserSimpleResponseDto user,
	String content,
	LocalDateTime createdAt
) {
	public static CommentResponseDto from(final Comment comment) {
		return new CommentResponseDto(comment.getId(),
			UserSimpleResponseDto.from(comment.getUser()),
			comment.getContent(),
			comment.getCreatedAt()
		);
	}
}
