package taco.klkl.domain.region.dto.response;

import taco.klkl.domain.region.domain.City;

/**
 *
 * @param id
 * @param name
 */
public record CityResponse(
	Long id,
	String name
) {
	public static CityResponse from(City city) {
		return new CityResponse(
			city.getId(),
			city.getName().getKoreanName()
		);
	}

}
