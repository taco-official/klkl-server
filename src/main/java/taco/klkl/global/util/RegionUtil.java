package taco.klkl.global.util;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import taco.klkl.domain.region.domain.region.Region;
import taco.klkl.domain.region.dto.response.country.CountryDetailResponse;

public class RegionUtil {

	public static List<CountryDetailResponse> generateCountriesByRegion(final Region region) {
		return Optional.ofNullable(region.getCountries())
			.map(regions -> regions.stream()
				.map(CountryDetailResponse::from)
				.toList())
			.orElse(Collections.emptyList());
	}
}
