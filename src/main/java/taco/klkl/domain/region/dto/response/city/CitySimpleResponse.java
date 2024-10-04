package taco.klkl.domain.region.dto.response.city;

import taco.klkl.domain.region.domain.city.City;

/**
 *
 * @param id
 * @param name
 */
public record CitySimpleResponse(
	Long id,
	String name
) {
	public static CitySimpleResponse from(final City city) {
		return new CitySimpleResponse(
			city.getId(),
			city.getName()
		);
	}

}
