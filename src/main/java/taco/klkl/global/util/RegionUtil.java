package taco.klkl.global.util;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import taco.klkl.domain.region.domain.region.Region;
import taco.klkl.domain.region.dto.response.country.CountrySimpleResponse;

public class RegionUtil {

	public static List<CountrySimpleResponse> createCountriesByRegion(final Region region) {
		return Optional.ofNullable(region.getCountries())
			.map(regions -> regions.stream()
				.map(CountrySimpleResponse::from)
				.toList())
			.orElse(Collections.emptyList());
	}
}
