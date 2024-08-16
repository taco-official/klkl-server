package taco.klkl.domain.region.dto.response;

import taco.klkl.domain.region.domain.Country;

/**
 *
 * @param countryId
 * @param name
 * @param flag
 * @param photo
 * @param currency
 */
public record CountryResponse(
	Long countryId,
	String name,
	String flag,
	String photo,
	CurrencyResponse currency
) {
	/**
	 *
	 * @param country
	 * @return CountryResponse
	 */
	public static CountryResponse from(Country country) {
		return new CountryResponse(
			country.getCountryId(),
			country.getName().getKoreanName(),
			country.getFlag(),
			country.getPhoto(),
			CurrencyResponse.from(country.getCurrency()));
	}

}
