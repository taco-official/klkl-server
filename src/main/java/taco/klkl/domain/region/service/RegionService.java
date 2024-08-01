package taco.klkl.domain.region.service;

import java.util.List;

import org.springframework.stereotype.Service;

import taco.klkl.domain.region.dto.response.RegionResponseDto;
import taco.klkl.domain.region.dto.response.RegionSimpleResponseDto;

@Service
public interface RegionService {
	List<RegionSimpleResponseDto> getAllRegions();

	RegionSimpleResponseDto getRegionById(Long id);

	RegionSimpleResponseDto getRegionByName(String name);

	RegionResponseDto getRegionWithCountries(Long id);
}
