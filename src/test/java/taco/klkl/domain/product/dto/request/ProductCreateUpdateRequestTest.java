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
import taco.klkl.global.common.constants.ProductConstants;
import taco.klkl.global.common.constants.ProductValidationMessages;

class ProductCreateUpdateRequestTest {

	private Validator validator;

	@BeforeEach
	void setUp() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}

	@Test
	@DisplayName("유효한 ProductCreateUpdateRequest 생성 시 검증 통과")
	void testValidProductCreateRequestDto() {
		ProductCreateUpdateRequest requestDto = new ProductCreateUpdateRequest(
			"Valid Product Name",
			"Valid product description",
			"Valid address",
			100,
			5.0,
			1L,
			2L,
			3L,
			Set.of(1L, 2L)
		);

		Set<ConstraintViolation<ProductCreateUpdateRequest>> violations = validator.validate(requestDto);
		assertTrue(violations.isEmpty());
	}

	@Test
	@DisplayName("상품명이 null일 때 검증 실패")
	void testNullProductName() {
		ProductCreateUpdateRequest requestDto = new ProductCreateUpdateRequest(
			null,
			"Valid product description",
			"Valid address",
			100,
			5.0,
			1L,
			2L,
			3L,
			Set.of(1L, 2L)
		);

		Set<ConstraintViolation<ProductCreateUpdateRequest>> violations = validator.validate(requestDto);
		assertFalse(violations.isEmpty());

		boolean foundNotNullMessage = violations.stream()
			.anyMatch(violation -> violation.getMessage().equals(ProductValidationMessages.NAME_NOT_NULL));
		assertTrue(foundNotNullMessage, "Expected NAME_NOT_NULL message not found");
	}

	@Test
	@DisplayName("상품명이 빈 문자열일 때 검증 실패")
	void testEmptyProductName() {
		ProductCreateUpdateRequest requestDto = new ProductCreateUpdateRequest(
			"",
			"Valid product description",
			"Valid address",
			100,
			5.0,
			1L,
			2L,
			3L,
			Set.of(1L, 2L)
		);

		Set<ConstraintViolation<ProductCreateUpdateRequest>> violations = validator.validate(requestDto);
		assertFalse(violations.isEmpty());

		boolean foundNotBlankMessage = violations.stream()
			.anyMatch(violation -> violation.getMessage().equals(ProductValidationMessages.NAME_NOT_BLANK));
		assertTrue(foundNotBlankMessage, "Expected NAME_NOT_BLANK message not found");
	}

	@ParameterizedTest
	@ValueSource(ints = {101, 200, 1000})
	@DisplayName("상품명이 최대 길이를 초과할 때 검증 실패")
	void testProductNameOverMaxLength(int nameLength) {
		String longName = "a".repeat(nameLength);
		ProductCreateUpdateRequest requestDto = new ProductCreateUpdateRequest(
			longName,
			"Valid product description",
			"Valid address",
			100,
			5.0,
			1L,
			2L,
			3L,
			Set.of(1L, 2L)
		);

		Set<ConstraintViolation<ProductCreateUpdateRequest>> violations = validator.validate(requestDto);
		assertFalse(violations.isEmpty());
		assertEquals(ProductValidationMessages.NAME_SIZE, violations.iterator().next().getMessage());
	}

	@Test
	@DisplayName("상품 설명이 null일 때 검증 실패")
	void testNullProductDescription() {
		ProductCreateUpdateRequest requestDto = new ProductCreateUpdateRequest(
			"Valid Product Name",
			null,
			"Valid address",
			100,
			5.0,
			1L,
			2L,
			3L,
			Set.of(1L, 2L)
		);

		Set<ConstraintViolation<ProductCreateUpdateRequest>> violations = validator.validate(requestDto);
		assertFalse(violations.isEmpty());

		boolean foundNotNullMessage = violations.stream()
			.anyMatch(violation -> violation.getMessage().equals(ProductValidationMessages.DESCRIPTION_NOT_NULL));
		assertTrue(foundNotNullMessage, "Expected DESCRIPTION_NOT_NULL message not found");
	}

	@Test
	@DisplayName("상품 설명이 빈 문자열일 때 검증 실패")
	void testEmptyProductDescription() {
		ProductCreateUpdateRequest requestDto = new ProductCreateUpdateRequest(
			"Valid Product Name",
			"",
			"Valid address",
			100,
			5.0,
			1L,
			2L,
			3L,
			Set.of(1L, 2L)
		);

		Set<ConstraintViolation<ProductCreateUpdateRequest>> violations = validator.validate(requestDto);
		assertFalse(violations.isEmpty());

		boolean foundNotBlankMessage = violations.stream()
			.anyMatch(violation -> violation.getMessage().equals(ProductValidationMessages.DESCRIPTION_NOT_BLANK));
		assertTrue(foundNotBlankMessage, "Expected DESCRIPTION_NOT_BLANK message not found");
	}

	@ParameterizedTest
	@ValueSource(ints = {2001, 3000, 5000})
	@DisplayName("상품 설명이 최대 길이를 초과할 때 검증 실패")
	void testProductDescriptionOverMaxLength(int descriptionLength) {
		String longDescription = "a".repeat(descriptionLength);
		ProductCreateUpdateRequest requestDto = new ProductCreateUpdateRequest(
			"Valid Product Name",
			longDescription,
			"Valid address",
			100,
			5.0,
			1L,
			2L,
			3L,
			Set.of(1L, 2L)
		);

		Set<ConstraintViolation<ProductCreateUpdateRequest>> violations = validator.validate(requestDto);
		assertFalse(violations.isEmpty());
		assertEquals(ProductValidationMessages.DESCRIPTION_SIZE, violations.iterator().next().getMessage());
	}

	@Test
	@DisplayName("주소가 null일 때 검증 실패")
	void testNullAddress() {
		ProductCreateUpdateRequest requestDto = new ProductCreateUpdateRequest(
			"Valid Product Name",
			"Valid product description",
			null,
			100,
			5.0,
			1L,
			2L,
			3L,
			Set.of(1L, 2L)
		);

		Set<ConstraintViolation<ProductCreateUpdateRequest>> violations = validator.validate(requestDto);
		assertFalse(violations.isEmpty());

		boolean foundNotNullMessage = violations.stream()
			.anyMatch(violation -> violation.getMessage().equals(ProductValidationMessages.ADDRESS_NOT_NULL));
		assertTrue(foundNotNullMessage, "Expected ADDRESS_NOT_NULL message not found");
	}

	@Test
	@DisplayName("주소가 빈 문자열일 때 검증 통과")
	void testEmptyAddress() {
		ProductCreateUpdateRequest requestDto = new ProductCreateUpdateRequest(
			"Valid Product Name",
			"Valid product description",
			"",
			100,
			5.0,
			1L,
			2L,
			3L,
			Set.of(1L, 2L)
		);

		Set<ConstraintViolation<ProductCreateUpdateRequest>> violations = validator.validate(requestDto);
		assertTrue(violations.isEmpty());
	}

	@ParameterizedTest
	@ValueSource(ints = {0, 1, ProductConstants.ADDRESS_MAX_LENGTH})
	@DisplayName("주소가 최대 길이 이하일 때 검증 통과")
	void testAddressUnderMaxLength(int addressLength) {
		String address = "a".repeat(addressLength);
		ProductCreateUpdateRequest requestDto = new ProductCreateUpdateRequest(
			"Valid Product Name",
			"Valid product description",
			address,
			100,
			5.0,
			1L,
			2L,
			3L,
			Set.of(1L, 2L)
		);

		Set<ConstraintViolation<ProductCreateUpdateRequest>> violations = validator.validate(requestDto);
		assertTrue(violations.isEmpty());
	}

	@ParameterizedTest
	@ValueSource(ints = {101, 200, 500})
	@DisplayName("주소가 최대 길이를 초과할 때 검증 실패")
	void testAddressOverMaxLength(int addressLength) {
		String longAddress = "a".repeat(addressLength);
		ProductCreateUpdateRequest requestDto = new ProductCreateUpdateRequest(
			"Valid Product Name",
			"Valid product description",
			longAddress,
			100,
			5.0,
			1L,
			2L,
			3L,
			Set.of(1L, 2L)
		);

		Set<ConstraintViolation<ProductCreateUpdateRequest>> violations = validator.validate(requestDto);
		assertFalse(violations.isEmpty());
		assertEquals(ProductValidationMessages.ADDRESS_SIZE, violations.iterator().next().getMessage());
	}

	@Test
	@DisplayName("가격이 null일 때 검증 실패")
	void testNullPrice() {
		ProductCreateUpdateRequest requestDto = new ProductCreateUpdateRequest(
			"Valid Product Name",
			"Valid product description",
			"Valid address",
			null,
			5.0,
			1L,
			2L,
			3L,
			Set.of(1L, 2L)
		);

		Set<ConstraintViolation<ProductCreateUpdateRequest>> violations = validator.validate(requestDto);
		assertFalse(violations.isEmpty());

		boolean foundNotNullMessage = violations.stream()
			.anyMatch(violation -> violation.getMessage().equals(ProductValidationMessages.PRICE_NOT_NULL));
		assertTrue(foundNotNullMessage, "Expected PRICE_NOT_NULL message not found");
	}

	@ParameterizedTest
	@ValueSource(ints = {0, 100, 1000})
	@DisplayName("가격이 0 이상일 때 검증 통과")
	void testZeroOrPositivePrice() {
		ProductCreateUpdateRequest requestDto = new ProductCreateUpdateRequest(
			"Valid Product Name",
			"Valid product description",
			"Valid address",
			0,
			5.0,
			1L,
			2L,
			3L,
			Set.of(1L, 2L)
		);

		Set<ConstraintViolation<ProductCreateUpdateRequest>> violations = validator.validate(requestDto);
		assertTrue(violations.isEmpty());
	}

	@ParameterizedTest
	@ValueSource(ints = {-1, -100, -1000})
	@DisplayName("가격이 음수일 때 검증 실패")
	void testNegativePrice(int price) {
		ProductCreateUpdateRequest requestDto = new ProductCreateUpdateRequest(
			"Valid Product Name",
			"Valid product description",
			"Valid address",
			price,
			5.0,
			1L,
			2L,
			3L,
			Set.of(1L, 2L)
		);

		Set<ConstraintViolation<ProductCreateUpdateRequest>> violations = validator.validate(requestDto);
		assertFalse(violations.isEmpty());
		assertEquals(ProductValidationMessages.PRICE_POSITIVE_OR_ZERO, violations.iterator().next().getMessage());
	}

	@Test
	@DisplayName("평점이 null일 때 검증 실패")
	void testNullRating() {
		ProductCreateUpdateRequest requestDto = new ProductCreateUpdateRequest(
			"Valid Product Name",
			"Valid product description",
			"Valid address",
			100,
			null,
			1L,
			2L,
			3L,
			Set.of(1L, 2L)
		);

		Set<ConstraintViolation<ProductCreateUpdateRequest>> violations = validator.validate(requestDto);
		assertFalse(violations.isEmpty());
		assertEquals(ProductValidationMessages.RATING_NOT_NULL, violations.iterator().next().getMessage());
	}

	@Test
	@DisplayName("평점이 최댓값보다 클 때 검증 실패")
	void testRatingOverMaxValue() {
		ProductCreateUpdateRequest requestDto = new ProductCreateUpdateRequest(
			"Valid Product Name",
			"Valid product description",
			"Valid address",
			100,
			5.5,
			1L,
			2L,
			3L,
			Set.of(1L, 2L)
		);

		Set<ConstraintViolation<ProductCreateUpdateRequest>> violations = validator.validate(requestDto);
		assertFalse(violations.isEmpty());
		assertEquals(ProductValidationMessages.RATING_OVER_MAX, violations.iterator().next().getMessage());
	}

	@Test
	@DisplayName("평점이 최솟값보다 작을 때 검증 실패")
	void testRatingUnderMinValue() {
		ProductCreateUpdateRequest requestDto = new ProductCreateUpdateRequest(
			"Valid Product Name",
			"Valid product description",
			"Valid address",
			100,
			0.0,
			1L,
			2L,
			3L,
			Set.of(1L, 2L)
		);

		Set<ConstraintViolation<ProductCreateUpdateRequest>> violations = validator.validate(requestDto);
		assertFalse(violations.isEmpty());
		assertEquals(ProductValidationMessages.RATING_UNDER_MIN, violations.iterator().next().getMessage());
	}

	@Test
	@DisplayName("도시 ID가 null일 때 검증 실패")
	void testNullCityId() {
		ProductCreateUpdateRequest requestDto = new ProductCreateUpdateRequest(
			"Valid Product Name",
			"Valid product description",
			"Valid address",
			100,
			5.0,
			null,
			2L,
			3L,
			Set.of(1L, 2L)
		);

		Set<ConstraintViolation<ProductCreateUpdateRequest>> violations = validator.validate(requestDto);
		assertFalse(violations.isEmpty());
		assertEquals(ProductValidationMessages.CITY_ID_NOT_NULL, violations.iterator().next().getMessage());
	}

	@Test
	@DisplayName("상품 소분류 ID가 null일 때 검증 실패")
	void testNullSubcategoryId() {
		ProductCreateUpdateRequest requestDto = new ProductCreateUpdateRequest(
			"Valid Product Name",
			"Valid product description",
			"Valid address",
			100,
			5.0,
			1L,
			null,
			3L,
			Set.of(1L, 2L)
		);

		Set<ConstraintViolation<ProductCreateUpdateRequest>> violations = validator.validate(requestDto);
		assertFalse(violations.isEmpty());
		assertEquals(ProductValidationMessages.SUBCATEGORY_ID_NOT_NULL, violations.iterator().next().getMessage());
	}

	@Test
	@DisplayName("통화 ID가 null일 때 검증 실패")
	void testNullCurrencyId() {
		ProductCreateUpdateRequest requestDto = new ProductCreateUpdateRequest(
			"Valid Product Name",
			"Valid product description",
			"Valid address",
			100,
			5.0,
			1L,
			2L,
			null,
			Set.of(1L, 2L)
		);

		Set<ConstraintViolation<ProductCreateUpdateRequest>> violations = validator.validate(requestDto);
		assertFalse(violations.isEmpty());
		assertEquals(ProductValidationMessages.CURRENCY_ID_NOT_NULL, violations.iterator().next().getMessage());
	}

	@Test
	@DisplayName("태그 ID가 null일 때 검증 실패")
	void testNullTagIds() {
		ProductCreateUpdateRequest requestDto = new ProductCreateUpdateRequest(
			"Valid Product Name",
			"Valid product description",
			"Valid address",
			100,
			5.0,
			1L,
			2L,
			3L,
			null
		);

		Set<ConstraintViolation<ProductCreateUpdateRequest>> violations = validator.validate(requestDto);
		assertTrue(violations.isEmpty());
	}
}
