package taco.klkl.domain.region.service;

import java.util.List;

import org.springframework.stereotype.Service;

import taco.klkl.domain.region.dto.response.RegionResponseDto;

@Service
public interface RegionService {
	public List<RegionResponseDto> getAllRegions();

	public RegionResponseDto getRegionById(Long id);

	public RegionResponseDto getRegionByName(String name);
}
