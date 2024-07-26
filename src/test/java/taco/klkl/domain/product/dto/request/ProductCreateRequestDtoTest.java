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

class ProductCreateRequestDtoTest {
	private Validator validator;

	@BeforeEach
	void setUp() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}

	@Test
	@DisplayName("모든 필수 필드가 존재할 때 유효성 검사 성공")
	void testProductCreateRequestDtoWithAllRequiredFields() {
		// given
		String name = "맛있는 곤약젤리";
		String description = "탱글탱글 맛있는 곤약젤리";
		Long cityId = 1L;
		Long subcategoryId = 2L;
		Long currencyId = 3L;
		String address = "신사이바시 메가돈키호테";
		Integer price = 100;

		ProductCreateRequestDto dto = new ProductCreateRequestDto(name, description, cityId, subcategoryId, currencyId,
			address, price);

		// when
		Set<ConstraintViolation<ProductCreateRequestDto>> violations = validator.validate(dto);

		// then
		assertTrue(violations.isEmpty());
	}

	@Test
	@DisplayName("상품명이 null일 때 유효성 검사 실패")
	void testProductCreateRequestDtoWithNullName() {
		// given
		ProductCreateRequestDto dto = new ProductCreateRequestDto(
			null,
			"탱글탱글 맛있는 곤약젤리",
			1L,
			2L,
			3L,
			"신사이바시 메가돈키호테",
			100
		);

		// when
		Set<ConstraintViolation<ProductCreateRequestDto>> violations = validator.validate(dto);

		// then
		assertFalse(violations.isEmpty());
		assertEquals("상품명은 필수 항목입니다.", violations.iterator().next().getMessage());
	}

	@Test
	@DisplayName("상품 설명이 null일 때 유효성 검사 실패")
	void testProductCreateRequestDtoWithNullDescription() {
		// given
		ProductCreateRequestDto dto = new ProductCreateRequestDto(
			"맛있는 곤약젤리",
			null,
			1L,
			2L,
			3L,
			"신사이바시 메가돈키호테",
			100
		);

		// when
		Set<ConstraintViolation<ProductCreateRequestDto>> violations = validator.validate(dto);

		// then
		assertFalse(violations.isEmpty());
		assertEquals("상품 설명은 필수 항목입니다.", violations.iterator().next().getMessage());
	}

	@Test
	@DisplayName("도시 ID가 null일 때 유효성 검사 실패")
	void testProductCreateRequestDtoWithNullCityId() {
		// given
		ProductCreateRequestDto dto = new ProductCreateRequestDto(
			"맛있는 곤약젤리",
			"탱글탱글 맛있는 곤약젤리",
			null,
			2L,
			3L,
			"신사이바시 메가돈키호테",
			100
		);

		// when
		Set<ConstraintViolation<ProductCreateRequestDto>> violations = validator.validate(dto);

		// then
		assertFalse(violations.isEmpty());
		assertEquals("도시 ID는 필수 항목입니다.", violations.iterator().next().getMessage());
	}

	@Test
	@DisplayName("상품 소분류 ID가 null일 때 유효성 검사 실패")
	void testProductCreateRequestDtoWithNullSubcategoryId() {
		// given
		ProductCreateRequestDto dto = new ProductCreateRequestDto(
			"맛있는 곤약젤리",
			"탱글탱글 맛있는 곤약젤리",
			1L,
			null,
			3L,
			"신사이바시 메가돈키호테",
			100
		);

		// when
		Set<ConstraintViolation<ProductCreateRequestDto>> violations = validator.validate(dto);

		// then
		assertFalse(violations.isEmpty());
		assertEquals("상품 소분류 ID은 필수 항목입니다.", violations.iterator().next().getMessage());
	}

	@Test
	@DisplayName("통화 ID가 null일 때 유효성 검사 실패")
	void testProductCreateRequestDtoWithNullCurrencyId() {
		// given
		ProductCreateRequestDto dto = new ProductCreateRequestDto(
			"맛있는 곤약젤리",
			"탱글탱글 맛있는 곤약젤리",
			1L,
			2L,
			null,
			"신사이바시 메가돈키호테",
			100
		);

		// when
		Set<ConstraintViolation<ProductCreateRequestDto>> violations = validator.validate(dto);

		// then
		assertFalse(violations.isEmpty());
		assertEquals("통화 ID는 필수 항목입니다.", violations.iterator().next().getMessage());
	}

	@Test
	@DisplayName("여러 필수 필드가 null일 때 유효성 검사 실패")
	void testProductCreateRequestDtoWithMultipleNullFields() {
		// given
		ProductCreateRequestDto dto = new ProductCreateRequestDto(
			null,
			null,
			null,
			null,
			null,
			"address",
			100
		);

		// when
		Set<ConstraintViolation<ProductCreateRequestDto>> violations = validator.validate(dto);

		// then
		assertEquals(5, violations.size());
		assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("상품명은 필수 항목입니다.")));
		assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("상품 설명은 필수 항목입니다.")));
		assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("도시 ID는 필수 항목입니다.")));
		assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("상품 소분류 ID은 필수 항목입니다.")));
		assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("통화 ID는 필수 항목입니다.")));
	}
}
