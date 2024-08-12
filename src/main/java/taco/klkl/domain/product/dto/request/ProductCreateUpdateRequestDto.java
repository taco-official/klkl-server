package taco.klkl.domain.product.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import taco.klkl.global.common.constants.ProductConstants;
import taco.klkl.global.common.constants.ProductValidationMessages;

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
public record ProductCreateUpdateRequestDto(
	@NotNull(message = ProductValidationMessages.NAME_NOT_NULL)
	@NotBlank(message = ProductValidationMessages.NAME_NOT_BLANK)
	@Size(max = ProductConstants.NAME_MAX_LENGTH, message = ProductValidationMessages.NAME_SIZE)
	String name,

	@NotNull(message = ProductValidationMessages.DESCRIPTION_NOT_NULL)
	@NotBlank(message = ProductValidationMessages.DESCRIPTION_NOT_BLANK)
	@Size(max = ProductConstants.DESCRIPTION_MAX_LENGTH, message = ProductValidationMessages.DESCRIPTION_SIZE)
	String description,

	@NotNull(message = ProductValidationMessages.ADDRESS_NOT_NULL)
	@Size(max = ProductConstants.ADDRESS_MAX_LENGTH, message = ProductValidationMessages.ADDRESS_SIZE)
	String address,

	@NotNull(message = ProductValidationMessages.PRICE_NOT_NULL)
	@PositiveOrZero(message = ProductValidationMessages.PRICE_POSITIVE_OR_ZERO)
	Integer price,

	@NotNull(message = ProductValidationMessages.CITY_ID_NOT_NULL)
	Long cityId,

	@NotNull(message = ProductValidationMessages.SUBCATEGORY_ID_NOT_NULL)
	Long subcategoryId,

	@NotNull(message = ProductValidationMessages.CURRENCY_ID_NOT_NULL)
	Long currencyId
) {
}
