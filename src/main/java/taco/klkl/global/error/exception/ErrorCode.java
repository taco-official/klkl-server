package taco.klkl.global.error.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
	// Common
	METHOD_ARGUMENT_INVALID(HttpStatus.BAD_REQUEST, "C010", "유효하지 않은 method 인자 입니다."),
	METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "C011", "지원하지 않는 HTTP method 입니다."),
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "C012", "서버에 문제가 발생했습니다. 관리자에게 문의해주세요."),

	// User

	// Product
	PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "C200", "존재하지 않는 상품입니다."),

	// Like

	// Comment

	// Region

	// Category

	// Filter

	// Notification

	// Search

	// Sample
	SAMPLE_ERROR(HttpStatus.BAD_REQUEST, "C999", "샘플 에러입니다."),
	;

	private final HttpStatus status;
	private final String code;
	private final String message;
}
