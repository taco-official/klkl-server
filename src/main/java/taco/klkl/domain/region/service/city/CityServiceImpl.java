package taco.klkl.domain.region.service.city;

import java.util.List;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import taco.klkl.domain.region.dao.city.CityRepository;
import taco.klkl.domain.region.domain.city.City;
import taco.klkl.domain.region.dto.response.city.CityHierarchyResponse;
import taco.klkl.domain.region.dto.response.city.CitySimpleResponse;
import taco.klkl.domain.region.exception.city.CityNotFoundException;

@Slf4j
@Primary
@Service
@Transactional
@RequiredArgsConstructor
public class CityServiceImpl implements CityService {

	private final CityRepository cityRepository;

	@Override
	public List<CitySimpleResponse> findAllCitiesByPartialString(final String partialString) {
		if (partialString == null || partialString.isEmpty()) {
			return List.of();
		}
		final List<City> cities = cityRepository.findAllByNameContaining(partialString);
		return cities.stream()
			.map(CitySimpleResponse::from)
			.toList();
	}

	@Override
	public CityHierarchyResponse findCityHierarchyById(final Long id) {
		final City city = cityRepository.findById(id)
			.orElseThrow(CityNotFoundException::new);
		return CityHierarchyResponse.from(city);
	}
}
