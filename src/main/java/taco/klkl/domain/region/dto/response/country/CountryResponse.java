package taco.klkl.domain.region.dto.response.country;

import taco.klkl.domain.region.domain.FlagUrlGenerator;
import taco.klkl.domain.region.domain.country.Country;
import taco.klkl.domain.region.dto.response.currency.CurrencyResponse;

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
			country.getName(),
			FlagUrlGenerator.generateSvgUrlByCountryCode(country.getCode()),
			country.getPhoto(),
			CurrencyResponse.from(country.getCurrency()));
	}

}
