package taco.klkl.domain.user.dto.response;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import taco.klkl.domain.user.domain.User;
import taco.klkl.global.common.enums.Gender;

class UserSimpleResponseDtoTest {
	@Test
	@DisplayName("유저 간단 정보를 반환하는 DTO 테스트")
	public void testUserSimpleResponseDto() {
		// given
		Long id = 1L;
		String profile = "image/profile.png";
		String name = "이름";

		// when
		UserSimpleResponseDto userSimple = new UserSimpleResponseDto(id, profile, name);

		// then
		assertThat(userSimple.id()).isEqualTo(id);
		assertThat(userSimple.profile()).isEqualTo(profile);
		assertThat(userSimple.name()).isEqualTo(name);
	}

	@Test
	@DisplayName("User 객체로부터 UserSimpleResponseDto 생성 테스트")
	public void testFrom() {
		// given
		String profile = "image/profile.png";
		String name = "이름";
		Gender gender = Gender.MALE;
		int age = 20;
		String description = "자기소개";

		// when
		User user = new User(profile, name, gender, age, description);
		UserSimpleResponseDto userSimple = UserSimpleResponseDto.from(user);

		// then
		assertThat(userSimple.profile()).isEqualTo(profile);
		assertThat(userSimple.name()).isEqualTo(name);
	}
}
