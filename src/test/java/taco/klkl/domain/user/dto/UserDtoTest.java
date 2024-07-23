package taco.klkl.domain.user.dto;

import static org.assertj.core.api.Assertions.*;

import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import taco.klkl.domain.user.dto.request.UserCreateRequestDto;

class UserDtoTest {
	private final Validator validator;

	public UserDtoTest() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		this.validator = factory.getValidator();
	}

	@Test
	@DisplayName("유효한 UserCreateRequestDto에 대한 유효성 검사")
	public void testValidUserCreateRequestDto() {
		// given
		UserCreateRequestDto requestDto = new UserCreateRequestDto("이름", "남", 20, "image/profile.png", "자기소개");

		// when
		Set<ConstraintViolation<UserCreateRequestDto>> violations = validator.validate(requestDto);

		// then
		assertThat(violations).isEmpty();
	}

	@Test
	@DisplayName("이름이 없는 UserCreateRequestDto 유효성 검사")
	public void testInvalidUserCreateRequestDto_NameRequired() {
		// given
		UserCreateRequestDto requestDto = new UserCreateRequestDto("", "남", 20, "image/profile.png", "자기소개");

		// when
		Set<ConstraintViolation<UserCreateRequestDto>> violations = validator.validate(requestDto);

		// then
		assertThat(violations).isNotEmpty();
	}

	@Test
	@DisplayName("나이가 음수인 UserCreateRequestDto 유효성 검사")
	public void testInvalidUserCreateRequestDto_AgeNegative() {
		// given
		UserCreateRequestDto requestDto = new UserCreateRequestDto("이름", "남", -1, "image/profile.png", "자기소개");

		// when
		Set<ConstraintViolation<UserCreateRequestDto>> violations = validator.validate(requestDto);

		// then
		assertThat(violations).isNotEmpty();
	}
}
