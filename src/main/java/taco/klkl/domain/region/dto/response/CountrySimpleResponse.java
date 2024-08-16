package taco.klkl.domain.region.dto.response;

import taco.klkl.domain.region.domain.Country;

/**
 *
 * @param id
 * @param name
 */
public record CountrySimpleResponse(
	Long id,
	String name
) {
	public static CountrySimpleResponse from(final Country country) {
		return new CountrySimpleResponse(
			country.getId(),
			country.getName().getKoreanName()
		);
	}
}
