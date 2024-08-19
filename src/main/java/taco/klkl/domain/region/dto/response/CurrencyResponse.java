package taco.klkl.domain.region.dto.response;

import taco.klkl.domain.region.domain.Currency;

/**
 *
 * @param id
 * @param code
 * @param flag
 */
public record CurrencyResponse(
	Long id,
	String code,
	String flag
) {
	public static CurrencyResponse from(final Currency currency) {
		return new CurrencyResponse(
			currency.getId(),
			currency.getCode().getCodeName(),
			currency.getFlag()
		);
	}
}
