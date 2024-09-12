package taco.klkl.domain.region.dto.response.country;

import taco.klkl.domain.region.domain.country.Country;

public record CountrySimpleResponse(
	Long id,
	String name
) {
	public static CountrySimpleResponse from(final Country country) {
		return new CountrySimpleResponse(
			country.getId(),
			country.getName()
		);
	}
}
