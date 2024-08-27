package taco.klkl.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record UserUpdateRequest(
	@NotNull(message = "이름은 필수 항목입니다.") String name,
	@NotNull(message = "성별은 필수 항목입니다.") String gender,
	@PositiveOrZero(message = "나이는 0 이상이어야 합니다.") Integer age,
	@NotBlank(message = "프로필 사진 url은 비워둘 수 없습니다.") String profileImageUrl,
	String description
) {
}
