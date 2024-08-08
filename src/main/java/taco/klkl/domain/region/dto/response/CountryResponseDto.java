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
public record CountryResponseDto(
	Long countryId,
	String name,
	String flag,
	String photo,
	CurrencyResponseDto currency
) {
	/**
	 *
	 * @param country
	 * @return CountryResponseDto
	 */
	public static CountryResponseDto from(Country country) {
		return new CountryResponseDto(
			country.getCountryId(),
			country.getName().getKoreanName(),
			country.getFlag(),
			country.getPhoto(),
			CurrencyResponseDto.from(country.getCurrency()));
	}

}
