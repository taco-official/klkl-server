package taco.klkl.domain.comment.dto.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import taco.klkl.domain.comment.domain.Comment;
import taco.klkl.global.common.constants.DefaultConstants;

public record CommentNotificationResponse(
	String userName,
	String content,
	@JsonFormat(pattern = DefaultConstants.DEFAULT_DATETIME_FORMAT) LocalDateTime createdAt
) {
	public static CommentNotificationResponse from(final Comment comment) {
		return new CommentNotificationResponse(
			comment.getUser().getName(),
			comment.getContent(),
			comment.getCreatedAt()
		);
	}
}
