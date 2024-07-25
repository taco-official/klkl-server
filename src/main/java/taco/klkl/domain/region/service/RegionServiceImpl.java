package taco.klkl.domain.region.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import taco.klkl.domain.region.dao.RegionRepository;
import taco.klkl.domain.region.domain.Region;
import taco.klkl.domain.region.dto.response.RegionResponseDto;
import taco.klkl.domain.region.exception.RegionNotFoundException;
import taco.klkl.global.error.exception.ErrorCode;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class RegionServiceImpl implements RegionService {

	private final RegionRepository regionRepository;

	@Override
	public List<RegionResponseDto> getAllRegions() {
		List<Region> regions = regionRepository.findAllByOrderByRegionIdAsc();
		return regions.stream()
			.map(RegionResponseDto::from)
			.toList();
	}

	@Override
	public RegionResponseDto getRegionById(Long id) {
		Region region = regionRepository.findById(id)
			.orElseThrow(() -> new RegionNotFoundException(ErrorCode.REGION_NOT_FOUND));
		
		return RegionResponseDto.from(region);
	}

	@Override
	public RegionResponseDto getRegionByName(String name) {
		Region region = regionRepository.findFirstByName(name);

		if (region == null) {
			throw new RegionNotFoundException(ErrorCode.REGION_NOT_FOUND);
		}

		return RegionResponseDto.from(region);
	}
}
