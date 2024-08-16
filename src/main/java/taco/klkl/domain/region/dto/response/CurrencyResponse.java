package taco.klkl.domain.region.dto.response;

import taco.klkl.domain.region.domain.Currency;

/**
 *
 * @param currencyId
 * @param code
 * @param flag
 */
public record CurrencyResponse(
	Long currencyId,
	String code,
	String flag
) {
	public static CurrencyResponse from(Currency currency) {
		return new CurrencyResponse(
			currency.getCurrencyId(),
			currency.getCode().getCodeName(),
			currency.getFlag()
		);
	}
}
