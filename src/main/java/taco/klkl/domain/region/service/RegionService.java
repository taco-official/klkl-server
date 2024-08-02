package taco.klkl.domain.region.service;

import java.util.List;

import org.springframework.stereotype.Service;

import taco.klkl.domain.region.dto.response.CountryResponseDto;
import taco.klkl.domain.region.dto.response.RegionSimpleResponseDto;

@Service
public interface RegionService {
	List<RegionSimpleResponseDto> getAllRegions();

	RegionSimpleResponseDto getRegionById(final Long id);

	RegionSimpleResponseDto getRegionByName(final String name);

	List<CountryResponseDto> getCountriesByRegionId(final Long id);
}
