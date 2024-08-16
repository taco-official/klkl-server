package taco.klkl.domain.comment.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import taco.klkl.global.common.constants.CommentValidationMessages;

public record CommentCreateUpdateRequest(
	@NotNull(message = CommentValidationMessages.CONTENT_NOT_NULL)
	@NotBlank(message = CommentValidationMessages.CONTENT_NOT_BLANK)
	@Size(max = 400, message = CommentValidationMessages.CONTENT_SIZE)
	String content
) {
}
