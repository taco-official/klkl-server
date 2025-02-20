package taco.klkl.domain.region.dto.response.country;

import java.util.List;

import taco.klkl.domain.region.domain.country.Country;
import taco.klkl.domain.region.dto.response.city.CitySimpleResponse;
import taco.klkl.global.util.CountryUtil;

/**
 *
 * @param id
 * @param name
 * @param cities
 */
public record CountryDetailResponse(
	Long id,
	String name,
	List<CitySimpleResponse> cities
) {
	public static CountryDetailResponse from(final Country country) {
		return new CountryDetailResponse(
			country.getId(),
			country.getName(),
			CountryUtil.createCitiesByCountry(country)
		);
	}
}
