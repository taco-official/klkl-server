package taco.klkl.domain.region.dto.response;

import taco.klkl.domain.region.domain.City;

/**
 *
 * @param cityId
 * @param name
 */
public record CityResponseDto(
	Long cityId,
	String name
) {
	public static CityResponseDto from(City city) {
		return new CityResponseDto(
			city.getCityId(),
			city.getName().getKoreanName()
		);
	}

}
