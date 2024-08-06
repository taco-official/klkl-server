package taco.klkl.domain.region.service;

import java.util.List;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import taco.klkl.domain.region.dao.CityRepository;
import taco.klkl.domain.region.domain.City;
import taco.klkl.domain.region.dto.response.CityResponseDto;
import taco.klkl.domain.region.enums.CityType;

@Slf4j
@Primary
@Service
@Transactional
@RequiredArgsConstructor
public class CityServiceImpl implements CityService {

	private final CityRepository cityRepository;

	@Override
	public boolean existsCityById(Long id) {
		return cityRepository.existsById(id);
	}

	@Override
	public List<CityResponseDto> getAllCitiesByCityTypes(final List<CityType> cityTypes) {

		final List<City> findCities = cityRepository.findAllByNameIn(cityTypes);

		return findCities.stream()
			.map(CityResponseDto::from)
			.toList();
	}
}
