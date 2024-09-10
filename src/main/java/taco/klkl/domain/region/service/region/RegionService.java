package taco.klkl.domain.region.service.region;

import java.util.List;

import org.springframework.stereotype.Service;

import taco.klkl.domain.region.dto.response.region.RegionResponse;

@Service
public interface RegionService {

	List<RegionResponse> findAllRegions();

}
