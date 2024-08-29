package taco.klkl.domain.region.dto.response;

import taco.klkl.domain.region.domain.Country;
import taco.klkl.domain.region.domain.FlagUrlGenerator;

/**
 *
 * @param id
 * @param name
 * @param flagUrl
 * @param photo
 * @param currency
 */
public record CountryResponse(
	Long id,
	String name,
	String flagUrl,
	String photo,
	CurrencyResponse currency
) {
	/**
	 *
	 * @param country
	 * @return CountryResponse
	 */
	public static CountryResponse from(final Country country) {
		return new CountryResponse(
			country.getId(),
			country.getName().getKoreanName(),
			FlagUrlGenerator.generateSvgUrl(country.getCountryCode()),
			country.getPhoto(),
			CurrencyResponse.from(country.getCurrency()));
	}

}
