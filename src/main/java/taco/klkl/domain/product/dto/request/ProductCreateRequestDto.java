package taco.klkl.domain.product.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import taco.klkl.global.common.constants.ProductConstants;

/**
 * TODO: 상품필터속성 추가 해야함 (상품필터속성 테이블 개발 후)
 * TODO: 상품 컨트롤러에서 필터 서비스를 이용해서 조합 하는게 괜찮아 보입니다.
 * @param name
 * @param description
 * @param address
 * @param price
 * @param cityId
 * @param subcategoryId
 * @param currencyId
 */
public record ProductCreateRequestDto(
	@NotNull(message = "상품명은 필수 항목입니다.")
	@NotBlank(message = "상품명은 비어있을 수 없습니다.")
	@Size(max = ProductConstants.NAME_MAX_LENGTH, message = "상품명은 100자 이하여야 합니다.")
	String name,

	@NotNull(message = "상품 설명은 필수 항목입니다.")
	@NotBlank(message = "상품 설명은 비어있을 수 없습니다.")
	@Size(max = ProductConstants.DESCRIPTION_MAX_LENGTH, message = "상품 설명은 2000자 이하여야 합니다.")
	String description,

	@Size(max = ProductConstants.ADDRESS_MAX_LENGTH, message = "주소는 100자 이하여야 합니다.")
	String address,

	@PositiveOrZero(message = "가격은 0 이상이어야 합니다.")
	Integer price,

	@NotNull(message = "도시 ID는 필수 항목입니다.") Long cityId,
	@NotNull(message = "상품 소분류 ID은 필수 항목입니다.") Long subcategoryId,
	@NotNull(message = "통화 ID는 필수 항목입니다.") Long currencyId
) {
}
