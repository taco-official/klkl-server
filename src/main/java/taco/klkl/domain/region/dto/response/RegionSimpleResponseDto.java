package taco.klkl.domain.region.dto.response;

import taco.klkl.domain.region.domain.Region;

public record RegionSimpleResponseDto(
	Long regionId,
	String name
) {

	public static RegionSimpleResponseDto from(Region region) {
		return new RegionSimpleResponseDto(region.getRegionId(), region.getName().getName());
	}
}
