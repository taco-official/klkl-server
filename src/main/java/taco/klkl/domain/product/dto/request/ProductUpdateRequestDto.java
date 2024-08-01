package taco.klkl.domain.product.dto.request;

import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import taco.klkl.global.common.constants.ProductConstants;
import taco.klkl.global.common.constants.ProductValidationMessages;

public record ProductUpdateRequestDto(
	@Size(max = ProductConstants.NAME_MAX_LENGTH, message = ProductValidationMessages.NAME_SIZE)
	String name,

	@Size(max = ProductConstants.DESCRIPTION_MAX_LENGTH, message = ProductValidationMessages.DESCRIPTION_SIZE)
	String description,

	@Size(max = ProductConstants.ADDRESS_MAX_LENGTH, message = ProductValidationMessages.ADDRESS_SIZE)
	String address,

	@PositiveOrZero(message = ProductValidationMessages.PRICE_POSITIVE_OR_ZERO)
	Integer price,

	Long cityId,

	Long subcategoryId,

	Long currencyId

) {
}
