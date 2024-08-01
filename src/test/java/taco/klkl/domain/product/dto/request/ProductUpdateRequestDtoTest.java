package taco.klkl.domain.product.dto.request;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import taco.klkl.global.common.constants.ProductValidationMessages;

class ProductUpdateRequestDtoTest {

	private Validator validator;

	@BeforeEach
	void setUp() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}

	@Test
	@DisplayName("유효한 ProductUpdateRequestDto 생성 시 검증 통과")
	void validProductUpdateRequestDto() {
		ProductUpdateRequestDto dto = new ProductUpdateRequestDto(
			"Valid Product Name",
			"Valid product description",
			"Valid address",
			100,
			1L,
			2L,
			3L
		);

		Set<ConstraintViolation<ProductUpdateRequestDto>> violations = validator.validate(dto);
		assertTrue(violations.isEmpty());
	}

	@ParameterizedTest
	@ValueSource(ints = {101, 200, 1000})
	@DisplayName("상품명이 최대 길이를 초과할 때 검증 실패")
	void productNameExceedsMaxLength(int nameLength) {
		String longName = "a".repeat(nameLength);
		ProductUpdateRequestDto dto = new ProductUpdateRequestDto(
			longName,
			"Valid product description",
			"Valid address",
			100,
			1L,
			2L,
			3L
		);

		Set<ConstraintViolation<ProductUpdateRequestDto>> violations = validator.validate(dto);
		assertFalse(violations.isEmpty());
		assertTrue(violations.stream()
			.anyMatch(violation -> violation.getMessage().equals(ProductValidationMessages.NAME_SIZE)));
	}

	@ParameterizedTest
	@ValueSource(ints = {2001, 3000, 5000})
	@DisplayName("상품 설명이 최대 길이를 초과할 때 검증 실패")
	void productDescriptionExceedsMaxLength(int descriptionLength) {
		String longDescription = "a".repeat(descriptionLength);
		ProductUpdateRequestDto dto = new ProductUpdateRequestDto(
			"Valid Product Name",
			longDescription,
			"Valid address",
			100,
			1L,
			2L,
			3L
		);

		Set<ConstraintViolation<ProductUpdateRequestDto>> violations = validator.validate(dto);
		assertFalse(violations.isEmpty());
		assertTrue(violations.stream()
			.anyMatch(violation -> violation.getMessage().equals(ProductValidationMessages.DESCRIPTION_SIZE)));
	}

	@ParameterizedTest
	@ValueSource(ints = {101, 200, 500})
	@DisplayName("주소가 최대 길이를 초과할 때 검증 실패")
	void addressExceedsMaxLength(int addressLength) {
		String longAddress = "a".repeat(addressLength);
		ProductUpdateRequestDto dto = new ProductUpdateRequestDto(
			"Valid Product Name",
			"Valid product description",
			longAddress,
			100,
			1L,
			2L,
			3L
		);

		Set<ConstraintViolation<ProductUpdateRequestDto>> violations = validator.validate(dto);
		assertFalse(violations.isEmpty());
		assertTrue(violations.stream()
			.anyMatch(violation -> violation.getMessage().equals(ProductValidationMessages.ADDRESS_SIZE)));
	}

	@ParameterizedTest
	@ValueSource(ints = {-1, -100, -1000})
	@DisplayName("가격이 음수일 때 검증 실패")
	void negativePrice(int price) {
		ProductUpdateRequestDto dto = new ProductUpdateRequestDto(
			"Valid Product Name",
			"Valid product description",
			"Valid address",
			price,
			1L,
			2L,
			3L
		);

		Set<ConstraintViolation<ProductUpdateRequestDto>> violations = validator.validate(dto);
		assertFalse(violations.isEmpty());
		assertTrue(violations.stream()
			.anyMatch(violation -> violation.getMessage().equals(ProductValidationMessages.PRICE_POSITIVE_OR_ZERO)));
	}

	@Test
	@DisplayName("모든 필드가 null이어도 검증 통과 (부분 업데이트 허용)")
	void allFieldsNullShouldPass() {
		ProductUpdateRequestDto dto = new ProductUpdateRequestDto(
			null,
			null,
			null,
			null,
			null,
			null,
			null
		);

		Set<ConstraintViolation<ProductUpdateRequestDto>> violations = validator.validate(dto);
		assertTrue(violations.isEmpty());
	}
}
