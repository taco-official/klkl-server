package taco.klkl.domain.region.service;

import java.util.List;

import org.springframework.stereotype.Service;

import taco.klkl.domain.region.dto.response.CountryResponseDto;
import taco.klkl.domain.region.dto.response.RegionResponseDto;

@Service
public interface RegionService {
	List<RegionResponseDto> getAllRegions();

	RegionResponseDto getRegionById(final Long id);

	RegionResponseDto getRegionByName(final String name);

	List<CountryResponseDto> getCountriesByRegionId(final Long id);
}
