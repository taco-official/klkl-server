package taco.klkl.global.error.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
	// Common
	METHOD_ARGUMENT_INVALID(HttpStatus.BAD_REQUEST, "C010", "유효하지 않은 method 인자 입니다."),
	METHOD_NOT_SUPPORTED(HttpStatus.METHOD_NOT_ALLOWED, "C011", "지원하지 않는 HTTP method 입니다."),
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "C012", "서버에 문제가 발생했습니다. 관리자에게 문의해주세요."),
	HTTP_MESSAGE_NOT_READABLE(HttpStatus.BAD_REQUEST, "C013", "잘못된 요청 메시지 형식입니다."),
	QUERY_TYPE_MISMATCH(HttpStatus.BAD_REQUEST, "C014", "올바르지 않은 쿼리 타입 입니다."),
	QUERY_PARAM_INVALID(HttpStatus.BAD_REQUEST, "C015", "올바르지 않은 쿼리 파라미터 값입니다."),
	QUERY_PARAM_NOT_FOUND(HttpStatus.BAD_REQUEST, "C016", "쿼리 파라미터가 존재하지 않습니다."),

	// User

	// Product
	PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "C020", "존재하지 않는 상품입니다."),
	INVALID_CITY_IDS(HttpStatus.BAD_REQUEST, "C021", "선택한 도시들은 동일한 국가에 속하지 않습니다."),
	RATING_NOT_FOUND(HttpStatus.NOT_FOUND, "C022", "존재하지 않는 평점입니다."),

	// Like

	// Comment
	COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "C040", "존재하지 않는 댓글입니다."),
	COMMENT_PRODUCT_NOT_MATCH(HttpStatus.BAD_REQUEST, "C041", "다른 상품에 있는 댓글입니다."),

	// Region
	REGION_NOT_FOUND(HttpStatus.NOT_FOUND, "C050", "해당 지역을 찾을 수 없습니다."),
	COUNTRY_NOT_FOUND(HttpStatus.NOT_FOUND, "C051", "해당 국가를 찾을 수 없습니다."),
	CITY_NOT_FOUND(HttpStatus.NOT_FOUND, "C052", "해당 도시를 찾을 수 없습니다."),
	CURRENCY_NOT_FOUND(HttpStatus.NOT_FOUND, "C053", "해당 통화를 찾을 수 없습니다."),

	// Category
	CATEGORY_ID_NOT_FOUND(HttpStatus.NOT_FOUND, "C060", "존재하지 않는 카테고리 ID 입니다."),
	SUBCATEGORY_ID_NOT_FOUND(HttpStatus.NOT_FOUND, "C061", "존재하지 않는 서브카테고리 ID 입니다."),
	FILTER_NOT_FOUND(HttpStatus.NOT_FOUND, "C062", "존재하지 않는 필터입니다."),

	// Notification

	// Search

	// Sample
	SAMPLE_ERROR(HttpStatus.BAD_REQUEST, "C999", "샘플 에러입니다."),
	;

	private final HttpStatus status;
	private final String code;
	private final String message;
}
