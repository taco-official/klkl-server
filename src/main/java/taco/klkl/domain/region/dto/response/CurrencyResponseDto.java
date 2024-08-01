package taco.klkl.domain.region.dto.response;

import taco.klkl.domain.region.domain.Currency;

public record CurrencyResponseDto(
	Long currencyId,
	String code,
	String flag
) {
	public static CurrencyResponseDto from(Currency currency) {
		return new CurrencyResponseDto(
			currency.getCurrencyId(),
			currency.getCode(),
			currency.getFlag()
		);
	}
}
