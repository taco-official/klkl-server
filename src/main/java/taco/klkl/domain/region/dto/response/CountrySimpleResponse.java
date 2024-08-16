package taco.klkl.domain.region.dto.response;

import taco.klkl.domain.region.domain.Country;

/**
 *
 * @param countryId
 * @param name
 */
public record CountrySimpleResponse(
	Long countryId,
	String name
) {
	public static CountrySimpleResponse from(Country country) {
		return new CountrySimpleResponse(
			country.getCountryId(),
			country.getName().getKoreanName()
		);
	}
}
