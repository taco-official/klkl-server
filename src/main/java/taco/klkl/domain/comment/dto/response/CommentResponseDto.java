package taco.klkl.domain.comment.dto.response;

import java.time.LocalDate;

import taco.klkl.domain.comment.domain.Comment;

public record CommentResponseDto(
	Long commentId,
	Long userId,
	String content,
	LocalDate createdAt
) {
	public static CommentResponseDto from(Comment comment) {
		return new CommentResponseDto(comment.getId(),
			comment.getUser().getId(),
			comment.getContent(),
			comment.getDate()
		);
	}
}
