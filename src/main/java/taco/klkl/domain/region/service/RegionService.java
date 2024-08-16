package taco.klkl.domain.region.service;

import java.util.List;

import org.springframework.stereotype.Service;

import taco.klkl.domain.region.dto.response.CountryResponse;
import taco.klkl.domain.region.dto.response.RegionResponse;

@Service
public interface RegionService {
	List<RegionResponse> getAllRegions();

	RegionResponse getRegionById(final Long id);

	RegionResponse getRegionByName(final String name);

	List<CountryResponse> getCountriesByRegionId(final Long id);
}
