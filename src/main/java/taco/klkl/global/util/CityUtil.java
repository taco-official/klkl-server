package taco.klkl.global.util;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import taco.klkl.domain.region.dao.city.CityRepository;
import taco.klkl.domain.region.domain.city.City;
import taco.klkl.domain.region.domain.country.Country;
import taco.klkl.domain.region.exception.city.CityNotFoundException;

@Component
@RequiredArgsConstructor
public class CityUtil {

	private final CityRepository cityRepository;

	public City findCityEntityById(final Long id) {
		return cityRepository.findById(id)
			.orElseThrow(CityNotFoundException::new);
	}

	public boolean isCitiesMappedToSameCountry(final Set<Long> cityIds) {
		final Set<Long> countryIds = cityIds.stream()
			.map(this::findCityEntityById)
			.map(City::getCountry)
			.map(Country::getId)
			.collect(Collectors.toSet());

		return countryIds.size() == 1;
	}
}
