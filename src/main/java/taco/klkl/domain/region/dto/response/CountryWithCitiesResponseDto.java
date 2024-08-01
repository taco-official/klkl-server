package taco.klkl.domain.region.dto.response;

import java.util.List;

import taco.klkl.domain.region.domain.Country;

public record CountryWithCitiesResponseDto(
	CountryWithOutRegionDto country,
	List<CityResponseDto> cities
) {
	public static CountryWithCitiesResponseDto from(Country country) {
		return new CountryWithCitiesResponseDto(
			CountryWithOutRegionDto.from(country),
			country.getCities().stream()
				.map(CityResponseDto::from)
				.toList()
		);
	}

}
