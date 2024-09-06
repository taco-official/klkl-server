package taco.klkl.domain.product.dto.request;

import static taco.klkl.global.common.constants.ProductConstants.*;
import static taco.klkl.global.common.constants.ProductValidationMessages.*;

import java.util.Set;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import taco.klkl.global.common.constants.ProductConstants;

/**
 * @param name
 * @param description
 * @param address
 * @param price
 * @param cityId
 * @param subcategoryId
 * @param currencyId
 */
public record ProductCreateUpdateRequest(
	@NotNull(message = NAME_NOT_NULL)
	@NotBlank(message = NAME_NOT_BLANK)
	@Size(max = ProductConstants.NAME_MAX_LENGTH, message = NAME_SIZE)
	String name,

	@NotNull(message = DESCRIPTION_NOT_NULL)
	@NotBlank(message = DESCRIPTION_NOT_BLANK)
	@Size(max = ProductConstants.DESCRIPTION_MAX_LENGTH, message = DESCRIPTION_SIZE)
	String description,

	@NotNull(message = ADDRESS_NOT_NULL)
	@Size(max = ProductConstants.ADDRESS_MAX_LENGTH, message = ADDRESS_SIZE)
	String address,

	@NotNull(message = PRICE_NOT_NULL)
	@PositiveOrZero(message = PRICE_POSITIVE_OR_ZERO)
	Integer price,

	@NotNull(message = RATING_NOT_NULL)
	@DecimalMin(value = RATING_MIN_VALUE, message = RATING_UNDER_MIN)
	@DecimalMax(value = RATING_MAX_VALUE, message = RATING_OVER_MAX)
	Double rating,

	@NotNull(message = CITY_ID_NOT_NULL)
	Long cityId,

	@NotNull(message = SUBCATEGORY_ID_NOT_NULL)
	Long subcategoryId,

	@NotNull(message = CURRENCY_ID_NOT_NULL)
	Long currencyId,

	Set<Long> tagIds
) {
}
