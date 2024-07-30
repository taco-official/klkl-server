package taco.klkl.domain.region.dto.response;

import java.util.List;

import taco.klkl.domain.region.domain.Region;

public record RegionResponseDto(
	Long regionId,
	String name,
	List<CountryWithOutRegionDto> countries
) {

	public static RegionResponseDto from(Region region) {
		return new RegionResponseDto(region.getRegionId(),
			region.getName().getName(),
			region.getCountries().stream()
				.map(CountryWithOutRegionDto::from).toList());
	}
}
