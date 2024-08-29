package taco.klkl.domain.region.dto.response;

import taco.klkl.domain.region.domain.Region;

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
