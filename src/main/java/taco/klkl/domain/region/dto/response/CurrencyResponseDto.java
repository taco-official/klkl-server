package taco.klkl.domain.region.dto.response;

import taco.klkl.domain.region.domain.Currency;

/**
 *
 * @param currencyId
 * @param code
 * @param flag
 */
public record CurrencyResponseDto(
	Long currencyId,
	String code,
	String flag
) {
	public static CurrencyResponseDto from(Currency currency) {
		return new CurrencyResponseDto(
			currency.getCurrencyId(),
			currency.getCode().getCodeName(),
			currency.getFlag()
		);
	}
}
