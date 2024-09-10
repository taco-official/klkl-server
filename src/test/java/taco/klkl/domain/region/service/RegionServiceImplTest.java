package taco.klkl.domain.region.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import taco.klkl.domain.region.dao.region.RegionRepository;
import taco.klkl.domain.region.domain.country.Country;
import taco.klkl.domain.region.domain.country.CountryType;
import taco.klkl.domain.region.domain.currency.Currency;
import taco.klkl.domain.region.domain.currency.CurrencyType;
import taco.klkl.domain.region.domain.region.Region;
import taco.klkl.domain.region.domain.region.RegionType;
import taco.klkl.domain.region.dto.response.country.CountryResponse;
import taco.klkl.domain.region.dto.response.region.RegionResponse;
import taco.klkl.domain.region.exception.region.RegionNotFoundException;
import taco.klkl.domain.region.service.region.RegionServiceImpl;

@ExtendWith(MockitoExtension.class)
class RegionServiceImplTest {

	@InjectMocks
	RegionServiceImpl regionService;

	@Mock
	RegionRepository regionRepository;

	private final Region region1 = Region.from(RegionType.NORTHEAST_ASIA);
	private final Region region2 = Region.from(RegionType.SOUTHEAST_ASIA);
	private final Region region3 = Region.from(RegionType.ETC);

	@Test
	@DisplayName("모든 지역 조회 성공 테스트")
	void testGetAllRegion() {
		// given
		List<Region> mockRegions = Arrays.asList(region1, region2, region3);

		when(regionRepository.findAllByOrderByIdAsc()).thenReturn(mockRegions);

		// when
		List<RegionResponse> regionResponses = regionService.findAllRegions();

		// then
		assertThat(regionResponses.size()).isEqualTo(3);
		assertThat(regionResponses.get(0).name()).isEqualTo(region1.getName());
		assertThat(regionResponses.get(1).name()).isEqualTo(region2.getName());
		assertThat(regionResponses.get(2).name()).isEqualTo(region3.getName());
	}

	@Test
	@DisplayName("모든 지역 조회 실패 테스트")
	void testGetAllRegionFail() {
		// given
		when(regionRepository.findAllByOrderByIdAsc()).thenReturn(Collections.emptyList());

		// when
		List<RegionResponse> regionResponses = regionService.findAllRegions();

		// then
		assertThat(regionResponses.size()).isEqualTo(0);
	}
}
