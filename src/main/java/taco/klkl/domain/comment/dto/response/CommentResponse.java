package taco.klkl.domain.comment.dto.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import taco.klkl.domain.comment.domain.Comment;
import taco.klkl.domain.user.dto.response.UserSimpleResponse;
import taco.klkl.global.common.constants.DefaultConstants;

public record CommentResponse(
	Long id,
	UserSimpleResponse user,
	String content,
	@JsonFormat(pattern = DefaultConstants.DEFAULT_DATETIME_FORMAT) LocalDateTime createdAt
) {
	public static CommentResponse from(final Comment comment) {
		return new CommentResponse(
			comment.getId(),
			UserSimpleResponse.from(comment.getUser()),
			comment.getContent(),
			comment.getCreatedAt()
		);
	}
}
