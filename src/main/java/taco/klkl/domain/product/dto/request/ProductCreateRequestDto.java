package taco.klkl.domain.product.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

/**
 * TODO: 상품필터속성 추가 해야함 (상품필터속성 테이블 개발 후)
 * TODO: 상품 컨트롤러에서 필터 서비스를 이용해서 조합 하는게 괜찮아 보입니다.
 * @param name
 * @param description
 * @param cityId
 * @param subcategoryId
 * @param currencyId
 * @param price
 * @param address
 */
public record ProductCreateRequestDto(
	@NotNull(message = "상품명은 필수 항목입니다.") String name,
	@NotNull(message = "상품 설명은 필수 항목입니다.") String description,
	@NotNull(message = "도시 ID는 필수 항목입니다.") Long cityId,
	@NotNull(message = "상품 소분류 ID은 필수 항목입니다.") Long subcategoryId,
	@NotNull(message = "통화 ID는 필수 항목입니다.") Long currencyId,
	@PositiveOrZero(message = "가격은 0 이상이어야 합니다.") Integer price,
	String address
) {
}
