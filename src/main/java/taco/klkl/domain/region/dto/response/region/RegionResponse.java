package taco.klkl.domain.region.dto.response.region;

import java.util.List;

import taco.klkl.domain.region.domain.region.Region;
import taco.klkl.domain.region.dto.response.country.CountryDetailResponse;
import taco.klkl.global.util.RegionUtil;

/**
 *
 * @param id
 * @param name
 * @param countries
 */
public record RegionResponse(
	Long id,
	String name,
	List<CountryDetailResponse> countries
) {

	public static RegionResponse from(final Region region) {
		return new RegionResponse(
			region.getId(),
			region.getName(),
			RegionUtil.createCountriesByRegion(region)
		);
	}
}
