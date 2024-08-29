package taco.klkl.domain.region.dto.response;

import taco.klkl.domain.region.domain.Currency;
import taco.klkl.domain.region.domain.FlagUrlGenerator;

/**
 *
 * @param id
 * @param code
 * @param koreanUnit
 * @param flagUrl
 */
public record CurrencyResponse(
	Long id,
	String code,
	String koreanUnit,
	String flagUrl
) {
	public static CurrencyResponse from(final Currency currency) {
		return new CurrencyResponse(
			currency.getId(),
			currency.getCode().getCode(),
			currency.getKoreanUnit(),
			FlagUrlGenerator.generateSvgUrlByCurrencyCode(currency.getCode().getCode())
		);
	}
}
