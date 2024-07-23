package taco.klkl.domain.product.dto.request;

import jakarta.validation.constraints.NotNull;

/**
 * TODO: 상품필터속성 추가 해야함 (상품필터속성 테이블 개발 후)
 * TODO: 상품 컨트롤러에서 필터 서비스를 이용해서 조합 하는게 괜찮아 보입니다.
 * @param name
 * @param description
 * @param cityId
 * @param subcategoryId
 * @param currencyId
 * @param address
 * @param price
 */
public record ProductCreateRequestDto(
	@NotNull(message = "name is required") String name,
	@NotNull(message = "description is required") String description,
	@NotNull(message = "name is required") Long cityId,
	@NotNull(message = "name is required") Long subcategoryId,
	@NotNull(message = "name is required") Long currencyId,
	@NotNull(message = "name is required") String address,
	@NotNull(message = "name is required") Long price
) {
}
