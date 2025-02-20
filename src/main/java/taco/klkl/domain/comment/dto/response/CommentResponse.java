package taco.klkl.domain.comment.dto.response;

import java.time.LocalDateTime;

import taco.klkl.domain.comment.domain.Comment;
import taco.klkl.domain.member.dto.response.MemberSimpleResponse;

public record CommentResponse(
	Long id,
	MemberSimpleResponse member,
	String content,
	LocalDateTime createdAt
) {
	public static CommentResponse from(final Comment comment) {
		return new CommentResponse(
			comment.getId(),
			MemberSimpleResponse.from(comment.getMember()),
			comment.getContent(),
			comment.getCreatedAt()
		);
	}
}
