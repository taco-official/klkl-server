package taco.klkl.domain.region.service;

import java.util.List;

import org.springframework.stereotype.Service;

import taco.klkl.domain.region.dto.response.CityResponseDto;
import taco.klkl.domain.region.enums.CityType;

@Service
public interface CityService {
	boolean existsCityById(final Long id);

	List<CityResponseDto> getAllCitiesByCityTypes(final List<CityType> cityTypes);
}
