package taco.klkl.domain.region.dto.response.region;

import java.util.List;

import taco.klkl.domain.region.domain.region.Region;
import taco.klkl.domain.region.dto.response.country.CountrySimpleResponse;

/**
 *
 * @param id
 * @param name
 * @param countries
 */
public record RegionResponse(
	Long id,
	String name,
	List<CountrySimpleResponse> countries
) {

	public static RegionResponse from(final Region region) {
		final List<CountrySimpleResponse> countries = region.getCountries().stream()
			.map(CountrySimpleResponse::from)
			.toList();

		return new RegionResponse(
			region.getId(),
			region.getName(),
			countries
		);
	}
}
