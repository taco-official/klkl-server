package taco.klkl.domain.region.dto.response;

import taco.klkl.domain.region.domain.Country;

/**
 *
 * @param id
 * @param name
 * @param flag
 * @param photo
 * @param currency
 */
public record CountryResponse(
	Long id,
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
	public static CountryResponse from(final Country country) {
		return new CountryResponse(
			country.getId(),
			country.getName().getKoreanName(),
			country.getFlag(),
			country.getPhoto(),
			CurrencyResponse.from(country.getCurrency()));
	}

}
