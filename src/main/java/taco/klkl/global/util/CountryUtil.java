package taco.klkl.global.util;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import taco.klkl.domain.region.dao.country.CountryRepository;
import taco.klkl.domain.region.domain.country.Country;
import taco.klkl.domain.region.dto.response.city.CitySimpleResponse;
import taco.klkl.domain.region.exception.country.CountryNotFoundException;

@Component
@RequiredArgsConstructor
public class CountryUtil {

	private final CountryRepository countryRepository;

	public Country findCountryEntityById(final Long id) {
		return countryRepository.findById(id)
			.orElseThrow(CountryNotFoundException::new);
	}

	public static List<CitySimpleResponse> createCitiesByCountry(final Country country) {
		return Optional.ofNullable(country.getCities())
			.map(cities -> cities.stream()
				.map(CitySimpleResponse::from)
				.toList())
			.orElse(Collections.emptyList());
	}
}
