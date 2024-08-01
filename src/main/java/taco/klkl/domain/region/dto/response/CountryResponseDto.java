package taco.klkl.domain.region.dto.response;

import taco.klkl.domain.region.domain.Country;

public record CountryResponseDto(
	Long countryId,
	RegionSimpleResponseDto region,
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
	public static CountryResponseDto from(Country country) {
		return new CountryResponseDto(
			country.getCountryId(),
			RegionSimpleResponseDto.from(country.getRegion()),
			country.getName().getKoreanName(),
			country.getFlag(),
			country.getPhoto(),
			country.getCurrencyId());
	}

}
