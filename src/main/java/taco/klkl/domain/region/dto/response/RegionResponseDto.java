package taco.klkl.domain.region.dto.response;

import taco.klkl.domain.region.domain.Region;

public record RegionResponseDto(
	Long regionId,
	String name
) {

	public static RegionResponseDto from(Region region) {
		return new RegionResponseDto(region.getRegionId(), region.getName());
	}
}
