package taco.klkl.domain.comment.dto.response;

import java.time.LocalDateTime;

import taco.klkl.domain.comment.domain.Comment;

public record CommentNotificationResponse(
	String userName,
	String content,
	LocalDateTime createdAt
) {
	public static CommentNotificationResponse from(final Comment comment) {
		return new CommentNotificationResponse(
			comment.getUser().getName(),
			comment.getContent(),
			comment.getCreatedAt()
		);
	}
}
