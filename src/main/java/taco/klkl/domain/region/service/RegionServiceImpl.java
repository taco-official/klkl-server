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
import taco.klkl.domain.region.domain.RegionType;
import taco.klkl.domain.region.dto.response.CountryResponse;
import taco.klkl.domain.region.dto.response.RegionResponse;
import taco.klkl.domain.region.exception.RegionNotFoundException;

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

	@Override
	public RegionResponse findRegionById(final Long id) throws RegionNotFoundException {
		final Region region = regionRepository.findById(id)
			.orElseThrow(RegionNotFoundException::new);
		return RegionResponse.from(region);
	}

	@Override
	public RegionResponse findRegionByName(final String name) throws RegionNotFoundException {
		final Region region = regionRepository.findFirstByName(RegionType.getRegionTypeByKoreanName(name));
		if (region == null) {
			throw new RegionNotFoundException();
		}
		return RegionResponse.from(region);
	}

	@Override
	public List<CountryResponse> findCountriesByRegionId(final Long id) {
		final Region findRegion = regionRepository.findById(id)
			.orElseThrow(RegionNotFoundException::new);
		final List<Country> countries = findRegion.getCountries();
		return countries.stream()
			.map(CountryResponse::from)
			.toList();
	}
}
