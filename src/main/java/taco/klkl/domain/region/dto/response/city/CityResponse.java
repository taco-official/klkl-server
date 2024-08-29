package taco.klkl.domain.region.dto.response.city;

import taco.klkl.domain.region.domain.city.City;

/**
 *
 * @param id
 * @param name
 */
public record CityResponse(
	Long id,
	String name
) {
	public static CityResponse from(final City city) {
		return new CityResponse(
			city.getId(),
			city.getName()
		);
	}

}
