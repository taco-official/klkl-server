package taco.klkl.global.util;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import taco.klkl.domain.region.dao.CityRepository;
import taco.klkl.domain.region.domain.City;
import taco.klkl.domain.region.domain.Country;
import taco.klkl.domain.region.exception.CityNotFoundException;

@Component
@RequiredArgsConstructor
public class CityUtil {

	private final CityRepository cityRepository;

	public City getCityEntityById(final Long id) {
		return cityRepository.findById(id)
			.orElseThrow(CityNotFoundException::new);
	}

	public boolean isCitiesMappedToSameCountry(final Set<Long> cityIds) {
		Set<Long> countryIds = cityIds.stream()
			.map(this::getCityEntityById)
			.map(City::getCountry)
			.map(Country::getCountryId)
			.collect(Collectors.toSet());

		if (countryIds.size() != 1) {
			return false;
		}
		return true;
	}
}
