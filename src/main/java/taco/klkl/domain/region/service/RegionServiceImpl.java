package taco.klkl.domain.region.service;

import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import taco.klkl.domain.region.dao.RegionRepository;
import taco.klkl.domain.region.domain.Country;
import taco.klkl.domain.region.domain.Region;
import taco.klkl.domain.region.dto.response.CountryResponseDto;
import taco.klkl.domain.region.dto.response.RegionResponseDto;
import taco.klkl.domain.region.enums.RegionType;
import taco.klkl.domain.region.exception.RegionNotFoundException;

@Slf4j
@Primary
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RegionServiceImpl implements RegionService {

	private final RegionRepository regionRepository;

	@Override
	public List<RegionResponseDto> getAllRegions() {

		final List<Region> regions = regionRepository.findAllByOrderByRegionIdAsc();

		if (regions == null) {
			return Collections.emptyList();
		}

		return regions.stream()
			.map(RegionResponseDto::from)
			.toList();
	}

	@Override
	public RegionResponseDto getRegionById(final Long id) throws RegionNotFoundException {

		final Region region = regionRepository.findById(id)
			.orElseThrow(RegionNotFoundException::new);

		return RegionResponseDto.from(region);
	}

	@Override
	public RegionResponseDto getRegionByName(final String name) throws RegionNotFoundException {

		final Region region = regionRepository.findFirstByName(RegionType.getRegionTypeByKoreanName(name));

		if (region == null) {
			throw new RegionNotFoundException();
		}

		return RegionResponseDto.from(region);
	}

	@Override
	public List<CountryResponseDto> getCountriesByRegionId(final Long id) {

		final Region findRegion = regionRepository.findById(id)
			.orElseThrow(RegionNotFoundException::new);

		final List<Country> countries = findRegion.getCountries();

		return countries.stream()
			.map(CountryResponseDto::from)
			.toList();
	}
}
