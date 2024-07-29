package taco.klkl.domain.product.dto.request;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

class ProductUpdateRequestDtoTest {
	private Validator validator;

	@BeforeEach
	void setUp() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}

	@Test
	@DisplayName("모든 필드가 유효한 값일 때 유효성 검사 성공")
	void testProductUpdateRequestDtoWithAllValidFields() {
		// given
		ProductUpdateRequestDto dto = new ProductUpdateRequestDto(
			"맛있는 곤약젤리",
			"탱글탱글 맛있는 곤약젤리",
			1L,
			2L,
			3L,
			"신사이바시 메가돈키호테",
			100
		);

		// when
		Set<ConstraintViolation<ProductUpdateRequestDto>> violations = validator.validate(dto);

		// then
		assertTrue(violations.isEmpty());
	}

	@Test
	@DisplayName("모든 필드가 null일 때 유효성 검사 성공")
	void testProductUpdateRequestDtoWithAllNullFields() {
		// given
		ProductUpdateRequestDto dto = new ProductUpdateRequestDto(
			null,
			null,
			null,
			null,
			null,
			null,
			null
		);

		// when
		Set<ConstraintViolation<ProductUpdateRequestDto>> violations = validator.validate(dto);

		// then
		assertTrue(violations.isEmpty());
	}

	@Test
	@DisplayName("일부 필드가 null일 때 유효성 검사 성공")
	void testProductUpdateRequestDtoWithSomeNullFields() {
		// given
		ProductUpdateRequestDto dto = new ProductUpdateRequestDto(
			"맛있는 곤약젤리",
			null,
			1L,
			null,
			3L,
			"신사이바시 메가돈키호테",
			null
		);

		// when
		Set<ConstraintViolation<ProductUpdateRequestDto>> violations = validator.validate(dto);

		// then
		assertTrue(violations.isEmpty());
	}
}
