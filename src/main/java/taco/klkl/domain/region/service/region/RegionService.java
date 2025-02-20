package taco.klkl.domain.region.service.region;

import java.util.List;

import taco.klkl.domain.region.dto.response.region.RegionResponse;

public interface RegionService {

	List<RegionResponse> findAllRegions();

}
