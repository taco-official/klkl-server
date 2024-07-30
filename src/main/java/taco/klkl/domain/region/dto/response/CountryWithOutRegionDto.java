package taco.klkl.domain.region.dto.response;

import taco.klkl.domain.region.domain.Country;

public record CountryWithOutRegionDto(
	Long countryId,
	String name,
	String flag,
	String photo,
	int currencyId
) {
	/**
	 *
	 * @param country
	 * @return CountryResponseDto
	 */
	public static CountryWithOutRegionDto from(Country country) {
		return new CountryWithOutRegionDto(
			country.getCountryId(),
			country.getName().getName(),
			country.getFlag(),
			country.getPhoto(),
			country.getCurrencyId());
	}
}
