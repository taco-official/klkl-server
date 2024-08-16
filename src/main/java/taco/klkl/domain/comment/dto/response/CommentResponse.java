package taco.klkl.domain.comment.dto.response;

import java.time.LocalDateTime;

import taco.klkl.domain.comment.domain.Comment;
import taco.klkl.domain.user.dto.response.UserSimpleResponse;

public record CommentResponse(
	Long commentId,
	UserSimpleResponse user,
	String content,
	LocalDateTime createdAt
) {
	public static CommentResponse from(final Comment comment) {
		return new CommentResponse(comment.getId(),
			UserSimpleResponse.from(comment.getUser()),
			comment.getContent(),
			comment.getCreatedAt()
		);
	}
}
