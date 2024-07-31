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

import taco.klkl.domain.region.dao.RegionRepository;
import taco.klkl.domain.region.domain.Country;
import taco.klkl.domain.region.domain.Region;
import taco.klkl.domain.region.dto.response.CountryWithOutRegionDto;
import taco.klkl.domain.region.dto.response.RegionResponseDto;
import taco.klkl.domain.region.dto.response.RegionSimpleResponseDto;
import taco.klkl.domain.region.enums.CountryType;
import taco.klkl.domain.region.enums.RegionType;
import taco.klkl.domain.region.exception.RegionNotFoundException;

@ExtendWith(MockitoExtension.class)
class RegionServiceImplTest {

	@InjectMocks
	RegionServiceImpl regionService;

	@Mock
	RegionRepository regionRepository;

	private final Region region1 = Region.of(RegionType.NORTHEAST_ASIA);
	private final Region region2 = Region.of(RegionType.SOUTHEAST_ASIA);
	private final Region region3 = Region.of(RegionType.ETC);
	private final Country country1 = Country.of(
		CountryType.JAPAN,
		region1,
		"image/japan",
		"image/japan",
		42);
	private final Country country2 = Country.of(
		CountryType.TAIWAN,
		region1,
		"image/taiwan",
		"image/taiwan",
		43);
	private final List<Country> countryList = Arrays.asList(country1,
		country2);

	@Test
	@DisplayName("모든 지역 조회 성공 테스트")
	void getAllRegionTest() {
		// given
		List<Region> mockRegions = Arrays.asList(region1, region2, region3);

		when(regionRepository.findAllByOrderByRegionIdAsc()).thenReturn(mockRegions);

		// when
		List<RegionSimpleResponseDto> regionResponseDtos = regionService.getAllRegions();

		// then
		assertThat(regionResponseDtos.size()).isEqualTo(3);
		assertThat(regionResponseDtos.get(0).name()).isEqualTo(region1.getName().getKoreanName());
		assertThat(regionResponseDtos.get(1).name()).isEqualTo(region2.getName().getKoreanName());
		assertThat(regionResponseDtos.get(2).name()).isEqualTo(region3.getName().getKoreanName());
	}

	@Test
	@DisplayName("모든 지역 조회 실패 테스트")
	void getAllRegionFailTest() {
		// given
		when(regionRepository.findAllByOrderByRegionIdAsc()).thenReturn(Collections.emptyList());

		// when
		List<RegionSimpleResponseDto> regionResponseDtos = regionService.getAllRegions();

		// then
		assertThat(regionResponseDtos.size()).isEqualTo(0);
	}

	@Test
	@DisplayName("Id 지역 조회 성공 테스트")
	void getRegionByIdTest() {
		// given
		when(regionRepository.findById(1L)).thenReturn(Optional.of(region1));
		when(regionRepository.findById(2L)).thenReturn(Optional.of(region2));

		// when
		RegionSimpleResponseDto region1ResponseDto = regionService.getRegionById(1L);
		RegionSimpleResponseDto region2ResponseDto = regionService.getRegionById(2L);

		// then
		assertThat(region1ResponseDto.name()).isEqualTo(region1.getName().getKoreanName());
		assertThat(region2ResponseDto.name()).isEqualTo(region2.getName().getKoreanName());
	}

	@Test
	@DisplayName("지역에 있는 도시목록 조회")
	void getRegionWithCountryTest() {
		// given
		Region mockRegion = mock(Region.class);
		when(mockRegion.getName()).thenReturn(RegionType.NORTHEAST_ASIA);
		when(regionRepository.findById(1L)).thenReturn(Optional.of(mockRegion));
		when(mockRegion.getCountries()).thenReturn(countryList);

		// when
		RegionResponseDto region1ResponseDto = regionService.getRegionsWithCountries(1L);

		// then
		assertThat(region1ResponseDto.name()).isEqualTo(mockRegion.getName().getKoreanName());
		assertThat(region1ResponseDto.countries().size()).isEqualTo(2);
		assertThat(region1ResponseDto.countries().get(0)).isEqualTo(CountryWithOutRegionDto.from(country1));
		assertThat(region1ResponseDto.countries().get(1)).isEqualTo(CountryWithOutRegionDto.from(country2));
	}

	@Test
	@DisplayName("Id 지역 조회 실패 테스트")
	void getRegionByIdFailTest() {
		// given
		when(regionRepository.findById(1L)).thenThrow(new RegionNotFoundException());

		// when & then
		Assertions.assertThrows(RegionNotFoundException.class, () -> {
			regionService.getRegionById(1L);
		});

		verify(regionRepository, times(1)).findById(1L);
	}

	@Test
	@DisplayName("Name 지역 조회 성공 테스트")
	void getRegionByNameTest() {
		// given
		when(regionRepository.findFirstByName(region1.getName())).thenReturn(region1);
		when(regionRepository.findFirstByName(region2.getName())).thenReturn(region2);

		// when
		RegionSimpleResponseDto region1ResponseDto = regionService.getRegionByName(region1.getName().getKoreanName());
		RegionSimpleResponseDto region2ResponseDto = regionService.getRegionByName(region2.getName().getKoreanName());

		// then
		assertThat(region1ResponseDto.name()).isEqualTo(region1.getName().getKoreanName());
		assertThat(region2ResponseDto.name()).isEqualTo(region2.getName().getKoreanName());
	}

	@Test
	@DisplayName("Name 지역 조회 실패 테스트")
	void getRegionByNameFailTest() {
		// given
		when(regionRepository.findFirstByName(region1.getName())).thenThrow(new RegionNotFoundException());

		// when & then
		Assertions.assertThrows(RegionNotFoundException.class, () -> {
			regionService.getRegionByName(region1.getName().getKoreanName());
		});

		verify(regionRepository, times(1)).findFirstByName(region1.getName());
	}
}
