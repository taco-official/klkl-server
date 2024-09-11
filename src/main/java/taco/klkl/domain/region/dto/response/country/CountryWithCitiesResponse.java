package taco.klkl.domain.region.dto.response.country;

import java.util.List;

import taco.klkl.domain.region.domain.country.Country;
import taco.klkl.domain.region.dto.response.city.CityResponse;
import taco.klkl.global.util.CountryUtil;

/**
 *
 * @param id
 * @param name
 * @param cities
 */
public record CountryWithCitiesResponse(
	Long id,
	String name,
	List<CityResponse> cities
) {
	public static CountryWithCitiesResponse from(final Country country) {
		return new CountryWithCitiesResponse(
			country.getId(),
			country.getName(),
			CountryUtil.createCitiesByCountry(country)
		);
	}
}
