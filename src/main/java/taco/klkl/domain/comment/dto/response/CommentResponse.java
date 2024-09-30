package taco.klkl.domain.comment.dto.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import taco.klkl.domain.comment.domain.Comment;
import taco.klkl.domain.member.dto.response.MemberSimpleResponse;
import taco.klkl.global.common.constants.DefaultConstants;

public record CommentResponse(
	Long id,
	MemberSimpleResponse member,
	String content,
	@JsonFormat(pattern = DefaultConstants.DEFAULT_DATETIME_FORMAT) LocalDateTime createdAt
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
