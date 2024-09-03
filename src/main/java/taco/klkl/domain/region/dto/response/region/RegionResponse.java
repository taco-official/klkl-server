package taco.klkl.domain.region.dto.response.region;

import taco.klkl.domain.region.domain.region.Region;

/**
 *
 * @param id
 * @param name
 */
public record RegionResponse(
	Long id,
	String name
) {

	public static RegionResponse from(final Region region) {
		return new RegionResponse(region.getId(), region.getName());
	}

}
