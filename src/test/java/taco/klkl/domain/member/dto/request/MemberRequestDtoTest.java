package taco.klkl.domain.member.dto.request;

import static org.assertj.core.api.Assertions.*;

import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

class MemberRequestDtoTest {
	private final Validator validator;

	public MemberRequestDtoTest() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		this.validator = factory.getValidator();
	}

	@Test
	@DisplayName("유효한 MemberCreateRequest에 대한 유효성 검사")
	public void testValidMemberCreateRequest() {
		// given
		MemberCreateRequest requestDto = new MemberCreateRequest("이름", "자기소개");

		// when
		Set<ConstraintViolation<MemberCreateRequest>> violations = validator.validate(requestDto);

		// then
		assertThat(violations).isEmpty();
	}

	@Test
	@DisplayName("이름이 null인 MemberCreateRequest 유효성 검사")
	public void testInvalidMemberCreateRequest_NameRequired() {
		// given
		MemberCreateRequest requestDto = new MemberCreateRequest(null, "자기소개");

		// when
		Set<ConstraintViolation<MemberCreateRequest>> violations = validator.validate(requestDto);

		// then
		assertThat(violations).isNotEmpty();
	}
}
