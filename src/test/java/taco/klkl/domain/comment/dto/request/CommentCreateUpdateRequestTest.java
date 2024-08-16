package taco.klkl.domain.comment.dto.request;

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
import taco.klkl.global.common.constants.CommentValidationMessages;

public class CommentCreateUpdateRequestTest {

	private Validator validator;

	@BeforeEach
	void setUp() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}

	@Test
	@DisplayName("내용이 널값일 경우")
	public void testNullContent() {
		//given
		final CommentCreateUpdateRequest requestDto = new CommentCreateUpdateRequest(
			null
		);

		//when & then
		Set<ConstraintViolation<CommentCreateUpdateRequest>> violations = validator.validate(requestDto);
		boolean foundNotNullMessage = violations.stream()
			.anyMatch(violation -> violation.getMessage().equals(CommentValidationMessages.CONTENT_NOT_NULL));

		assertTrue(foundNotNullMessage);
	}

	@Test
	@DisplayName("내용이 빈칸일 경우")
	public void testBlankContent() {
		//given
		final CommentCreateUpdateRequest requestDto = new CommentCreateUpdateRequest(
			" "
		);

		//when & then
		Set<ConstraintViolation<CommentCreateUpdateRequest>> violations = validator.validate(requestDto);
		boolean foundNotNullMessage = violations.stream()
			.anyMatch(violation -> violation.getMessage().equals(CommentValidationMessages.CONTENT_NOT_BLANK));

		assertTrue(foundNotNullMessage);
	}

	@ParameterizedTest
	@ValueSource(ints = {300, 400, 500})
	@DisplayName("내용이 길이가 너무 길 경우")
	public void testBigContent(int length) {
		//given
		String longContent = "hello".repeat(length);
		final CommentCreateUpdateRequest requestDto = new CommentCreateUpdateRequest(
			longContent
		);

		//when & then
		Set<ConstraintViolation<CommentCreateUpdateRequest>> violations = validator.validate(requestDto);
		boolean foundNotNullMessage = violations.stream()
			.anyMatch(violation -> violation.getMessage().equals(CommentValidationMessages.CONTENT_SIZE));

		assertTrue(foundNotNullMessage);
	}
}
