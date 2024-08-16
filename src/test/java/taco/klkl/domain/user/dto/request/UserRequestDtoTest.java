package taco.klkl.domain.user.dto.request;

import static org.assertj.core.api.Assertions.*;

import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

class UserRequestDtoTest {
	private final Validator validator;

	public UserRequestDtoTest() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		this.validator = factory.getValidator();
	}

	@Test
	@DisplayName("유효한 UserCreateRequestDto에 대한 유효성 검사")
	public void testValidUserCreateRequestDto() {
		// given
		UserCreateRequest requestDto = new UserCreateRequest("이름", "남", 20, "image/profile.png", "자기소개");

		// when
		Set<ConstraintViolation<UserCreateRequest>> violations = validator.validate(requestDto);

		// then
		assertThat(violations).isEmpty();
	}

	@Test
	@DisplayName("이름이 null인 UserCreateRequest 유효성 검사")
	public void testInvalidUserCreateRequestDto_NameRequired() {
		// given
		UserCreateRequest requestDto = new UserCreateRequest(null, "남", 20, "image/profile.png", "자기소개");

		// when
		Set<ConstraintViolation<UserCreateRequest>> violations = validator.validate(requestDto);

		// then
		assertThat(violations).isNotEmpty();
	}

	@Test
	@DisplayName("나이가 음수인 UserCreateRequest 유효성 검사")
	public void testInvalidUserCreateRequestDto_AgeNegative() {
		// given
		UserCreateRequest requestDto = new UserCreateRequest("이름", "남", -1, "image/profile.png", "자기소개");

		// when
		Set<ConstraintViolation<UserCreateRequest>> violations = validator.validate(requestDto);

		// then
		assertThat(violations).isNotEmpty();
	}
}
