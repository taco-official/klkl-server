package taco.klkl.domain.region.dto.response.country;

import java.util.List;

import taco.klkl.domain.region.domain.country.Country;
import taco.klkl.domain.region.dto.response.city.CityResponse;

/**
 *
 * @param id
 * @param name
 * @param cities
 */
public record CountrySimpleResponse(
	Long id,
	String name,
	List<CityResponse> cities
) {
	public static CountrySimpleResponse from(final Country country) {
		final List<CityResponse> cities = country.getCities().stream()
			.map(CityResponse::from)
			.toList();

		return new CountrySimpleResponse(
			country.getId(),
			country.getName(),
			cities
		);
	}
}
