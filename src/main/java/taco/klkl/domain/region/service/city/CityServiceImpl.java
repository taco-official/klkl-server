package taco.klkl.domain.region.service.city;

import java.util.List;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import taco.klkl.domain.region.dao.city.CityRepository;
import taco.klkl.domain.region.domain.city.City;
import taco.klkl.domain.region.dto.response.city.CityResponse;

@Slf4j
@Primary
@Service
@Transactional
@RequiredArgsConstructor
public class CityServiceImpl implements CityService {

	private final CityRepository cityRepository;

	@Override
	public List<CityResponse> findAllCitiesByPartialString(final String partialString) {

		if (partialString == null || partialString.isEmpty()) {
			return List.of();
		}

		final List<City> findCities = cityRepository.findAllByNameLike(partialString);

		return findCities.stream()
			.map(CityResponse::from)
			.toList();
	}
}
