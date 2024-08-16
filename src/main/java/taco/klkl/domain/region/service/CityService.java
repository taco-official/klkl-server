package taco.klkl.domain.region.service;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import taco.klkl.domain.region.domain.City;
import taco.klkl.domain.region.dto.response.CityResponseDto;
import taco.klkl.domain.region.enums.CityType;

@Service
public interface CityService {
	City getCityEntityById(final Long id);

	List<CityResponseDto> getAllCitiesByCityTypes(final List<CityType> cityTypes);

	boolean isCitiesMappedToSameCountry(final Set<Long> cityIds);
}
