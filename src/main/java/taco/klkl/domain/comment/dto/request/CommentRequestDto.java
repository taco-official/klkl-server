package taco.klkl.domain.comment.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import taco.klkl.domain.comment.annotation.ExistUser;
import taco.klkl.global.common.constants.CommentValidationMessage;

public record CommentRequestDto(
	@NotNull(message = CommentValidationMessage.USER_NOT_NULL)
	@ExistUser(message = CommentValidationMessage.USER_NOT_FOUND)
	Long userId,

	@NotNull(message = CommentValidationMessage.CONTENT_NOT_NULL)
	@NotBlank(message = CommentValidationMessage.CONTENT_NOT_BLANK)
	@Size(max = 400, message = CommentValidationMessage.CONTENT_SIZE)
	String content
) {
}
