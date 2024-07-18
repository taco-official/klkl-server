package taco.klkl.global.error.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
	// Sample
	SAMPLE_ERROR(HttpStatus.BAD_REQUEST, "C000", "샘플 에러입니다."),

	// Common
	METHOD_ARGUMENT_INVALID(HttpStatus.BAD_REQUEST, "C010", "유효하지 않은 method 인자 입니다."),
	METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "C011", "지원하지 않는 HTTP method 입니다."),
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "C012", "서버에 문제가 발생했습니다. 관리자에게 문의해주세요."),

	// User

	// Product

	// Like

	// Comment

	// Region

	// Category

	// Filter

	// Notification

	// Search
	;

	private final HttpStatus status;
	private final String code;
	private final String message;
}
