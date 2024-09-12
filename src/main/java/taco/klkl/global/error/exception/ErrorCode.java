package taco.klkl.global.error.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
	// Common
	METHOD_ARGUMENT_INVALID(HttpStatus.BAD_REQUEST, "유효하지 않은 method 인자 입니다."),
	METHOD_NOT_SUPPORTED(HttpStatus.METHOD_NOT_ALLOWED, "지원하지 않는 HTTP method 입니다."),
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버에 문제가 발생했습니다. 관리자에게 문의해주세요."),
	HTTP_MESSAGE_NOT_READABLE(HttpStatus.BAD_REQUEST, "잘못된 요청 메시지 형식입니다."),
	QUERY_TYPE_MISMATCH(HttpStatus.BAD_REQUEST, "올바르지 않은 쿼리 타입 입니다."),
	QUERY_PARAM_INVALID(HttpStatus.BAD_REQUEST, "올바르지 않은 쿼리 파라미터 값입니다."),
	QUERY_PARAM_NOT_FOUND(HttpStatus.BAD_REQUEST, "쿼리 파라미터가 존재하지 않습니다."),

	// Member
	MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다."),
	GENDER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 성별입니다."),
	SELF_FOLLOW_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "자기 자신을 팔로우할 수 없습니다."),

	// Product
	PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 상품입니다."),
	RATING_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 평점입니다."),
	SORT_CRITERIA_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 정렬 기준입니다."),
	SORT_DIRECTION_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 정렬 방향입니다."),
	INVALID_CITY_IDS(HttpStatus.BAD_REQUEST, "선택한 도시들은 동일한 국가에 속하지 않습니다."),
	PRODUCT_MEMBER_NOT_MATCH(HttpStatus.BAD_REQUEST, "다른 유저의 상품입니다."),

	// Like
	LIKE_COUNT_OVER_MAXIMUM(HttpStatus.BAD_REQUEST, "상품의 좋아요수가 최대값입니다. 2147483647"),
	LIKE_COUNT_BELOW_MINIMUM(HttpStatus.BAD_REQUEST, "상품의 좋아요수가 최소값입니다. 0"),

	// Comment
	COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 댓글입니다."),
	COMMENT_MEMBER_NOT_MATCH(HttpStatus.BAD_REQUEST, "다른 유저의 댓글입니다."),
	COMMENT_PRODUCT_NOT_MATCH(HttpStatus.BAD_REQUEST, "다른 상품에 있는 댓글입니다."),

	// Region
	REGION_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 지역을 찾을 수 없습니다."),
	COUNTRY_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 국가를 찾을 수 없습니다."),
	CITY_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 도시를 찾을 수 없습니다."),
	CURRENCY_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 통화를 찾을 수 없습니다."),
	CITY_TYPE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 도시 종류입니다."),
	COUNTRY_TYPE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 국가 종류입니다."),
	CURRENCY_TYPE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 통화 종류입니다."),
	REGION_TYPE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 지역 종류입니다."),

	// Category
	CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 카테고리 ID 입니다."),
	SUBCATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 서브카테고리 ID 입니다."),
	TAG_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 태그입니다."),
	TAG_TYPE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 카테코리 이름입니다."),
	SUBCATEGORY_TYPE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 서브카테코리 이름입니다."),
	TAG_NAME_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 태그 이름입니다."),

	// Notification
	NOTIFICATION_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 알림 입니다."),

	// Search

	// Image
	FILE_EXTENSION_NOT_FOUND(HttpStatus.NOT_FOUND, "유효하지 않은 파일 확장자입니다."),
	IMAGE_TYPE_NOT_FOUND(HttpStatus.NOT_FOUND, "유효하지 않은 이미지 타입입니다."),
	UPLOAD_STATE_NOT_FOUND(HttpStatus.NOT_FOUND, "유효하지 않은 업로드 상태입니다."),
	IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 이미지입니다."),
	IMAGE_UPLOAD_NOT_COMPLETE(HttpStatus.BAD_REQUEST, "이미지 업로드가 완료되지 않았습니다."),
	IMAGE_URL_INVALID(HttpStatus.BAD_REQUEST, "유효하지 않은 이미지 url 형식입니다."),

	// Sample
	SAMPLE_ERROR(HttpStatus.BAD_REQUEST, "샘플 에러입니다."),
	;

	private final HttpStatus httpStatus;
	private final String message;
}
