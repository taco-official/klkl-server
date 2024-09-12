package taco.klkl.domain.region.service.region;

import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import taco.klkl.domain.region.dao.region.RegionRepository;
import taco.klkl.domain.region.domain.region.Region;
import taco.klkl.domain.region.dto.response.region.RegionResponse;

@Slf4j
@Primary
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RegionServiceImpl implements RegionService {

	private final RegionRepository regionRepository;

	@Override
	public List<RegionResponse> findAllRegions() {
		final List<Region> regions = regionRepository.findAllByOrderByIdAsc();
		if (regions == null) {
			return Collections.emptyList();
		}
		return regions.stream()
			.map(RegionResponse::from)
			.toList();
	}
}
