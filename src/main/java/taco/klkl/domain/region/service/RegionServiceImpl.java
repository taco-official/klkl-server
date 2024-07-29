package taco.klkl.domain.region.service;

import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import taco.klkl.domain.region.dao.RegionRepository;
import taco.klkl.domain.region.domain.Region;
import taco.klkl.domain.region.dto.response.RegionResponseDto;
import taco.klkl.domain.region.enums.RegionType;
import taco.klkl.domain.region.exception.RegionNotFoundException;

@Slf4j
@Primary
@Service
@Transactional
@RequiredArgsConstructor
public class RegionServiceImpl implements RegionService {

	private final RegionRepository regionRepository;

	@Override
	public List<RegionResponseDto> getAllRegions() {
		List<Region> regions = regionRepository.findAllByOrderByRegionIdAsc();

		if (regions == null) {
			return Collections.emptyList();
		}

		return regions.stream()
			.map(RegionResponseDto::from)
			.toList();
	}

	@Override
	public RegionResponseDto getRegionById(Long id) throws RegionNotFoundException {
		Region region = regionRepository.findById(id)
			.orElseThrow(RegionNotFoundException::new);
		return RegionResponseDto.from(region);
	}

	@Override
	public RegionResponseDto getRegionByName(String name) throws RegionNotFoundException {
		Region region = regionRepository.findFirstByName(RegionType.getRegionByName(name));

		if (region == null) {
			throw new RegionNotFoundException();
		}

		return RegionResponseDto.from(region);
	}
}
