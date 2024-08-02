package taco.klkl.domain.region.dto.response;

import java.util.List;

import taco.klkl.domain.region.domain.Country;

public record CountryWithCitiesResponseDto(
	CountryResponseDto country,
	List<CityResponseDto> cities
) {
	public static CountryWithCitiesResponseDto from(Country country) {
		return new CountryWithCitiesResponseDto(
			CountryResponseDto.from(country),
			country.getCities().stream()
				.map(CityResponseDto::from)
				.toList()
		);
	}

}
