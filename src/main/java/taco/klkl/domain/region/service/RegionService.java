package taco.klkl.domain.region.service;

import java.util.List;

import org.springframework.stereotype.Service;

import taco.klkl.domain.region.dto.response.CountryResponse;
import taco.klkl.domain.region.dto.response.RegionResponse;

@Service
public interface RegionService {
	List<RegionResponse> findAllRegions();

	RegionResponse findRegionById(final Long id);

	RegionResponse findRegionByName(final String name);

	List<CountryResponse> findCountriesByRegionId(final Long id);
}
