package taco.klkl.domain.region.dto.response.currency;

import taco.klkl.domain.region.domain.FlagUrlGenerator;
import taco.klkl.domain.region.domain.currency.Currency;

/**
 *
 * @param id
 * @param code
 * @param unit
 * @param flagUrl
 */
public record CurrencyResponse(
	Long id,
	String code,
	String unit,
	String flagUrl
) {
	public static CurrencyResponse from(final Currency currency) {
		return new CurrencyResponse(
			currency.getId(),
			currency.getCode(),
			currency.getUnit(),
			FlagUrlGenerator.generateSvgUrlByCurrencyCode(currency.getCode())
		);
	}
}
