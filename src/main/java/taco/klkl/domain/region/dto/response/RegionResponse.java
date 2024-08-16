package taco.klkl.domain.region.dto.response;

import taco.klkl.domain.region.domain.Region;

/**
 *
 * @param regionId
 * @param name
 */
public record RegionResponse(
	Long regionId,
	String name
) {

	public static RegionResponse from(Region region) {
		return new RegionResponse(region.getRegionId(), region.getName().getKoreanName());
	}

}
