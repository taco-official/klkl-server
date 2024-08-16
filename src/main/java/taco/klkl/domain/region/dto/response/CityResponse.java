package taco.klkl.domain.region.dto.response;

import taco.klkl.domain.region.domain.City;

/**
 *
 * @param cityId
 * @param name
 */
public record CityResponse(
	Long cityId,
	String name
) {
	public static CityResponse from(City city) {
		return new CityResponse(
			city.getCityId(),
			city.getName().getKoreanName()
		);
	}

}
