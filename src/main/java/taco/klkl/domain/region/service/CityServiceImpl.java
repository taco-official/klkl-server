package taco.klkl.domain.region.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import taco.klkl.domain.region.dao.CityRepository;
import taco.klkl.domain.region.domain.City;
import taco.klkl.domain.region.domain.Country;
import taco.klkl.domain.region.dto.response.CityResponse;
import taco.klkl.domain.region.enums.CityType;
import taco.klkl.domain.region.exception.CityNotFoundException;

@Slf4j
@Primary
@Service
@Transactional
@RequiredArgsConstructor
public class CityServiceImpl implements CityService {

	private final CityRepository cityRepository;

	@Override
	public City getCityEntityById(final Long id) {
		return cityRepository.findById(id)
			.orElseThrow(CityNotFoundException::new);
	}

	@Override
	public List<CityResponse> getAllCitiesByCityTypes(final List<CityType> cityTypes) {

		if (cityTypes == null || cityTypes.isEmpty()) {
			return List.of();
		}

		final List<City> findCities = cityRepository.findAllByNameIn(cityTypes);

		return findCities.stream()
			.map(CityResponse::from)
			.toList();
	}

	@Override
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
