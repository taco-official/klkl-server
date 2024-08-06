package taco.klkl.domain.comment.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CommentRequestDto(
	@NotNull
	Long userId,

	@NotNull
	@NotBlank
	@Size(max = 400)
	String content
) {
}
