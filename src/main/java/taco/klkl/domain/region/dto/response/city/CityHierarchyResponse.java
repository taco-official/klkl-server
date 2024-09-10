package taco.klkl.domain.region.dto.response.city;

import taco.klkl.domain.region.domain.city.City;
import taco.klkl.domain.region.domain.country.Country;
import taco.klkl.domain.region.domain.region.Region;

public record CityHierarchyResponse(
	Long cityId,
	Long countryId,
	Long regionId
) {
	public static CityHierarchyResponse from(final City city) {
		final Country country = city.getCountry();
		final Region region = country.getRegion();

		return new CityHierarchyResponse(
			city.getId(),
			country.getId(),
			region.getId()
		);
	}
}
