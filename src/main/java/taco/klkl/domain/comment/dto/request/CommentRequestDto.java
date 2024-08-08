package taco.klkl.domain.comment.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import taco.klkl.domain.comment.annotation.ExistUser;
import taco.klkl.global.common.constants.CommentValidationMessage;

public record CommentRequestDto(
	@NotNull
	@ExistUser(message = CommentValidationMessage.USER_NOT_FOUND)
	Long userId,

	@NotNull
	@NotBlank
	@Size(max = 400)
	String content
) {
}
