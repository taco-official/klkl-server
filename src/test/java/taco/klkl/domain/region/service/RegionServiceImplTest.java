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
import taco.klkl.domain.region.domain.Region;
import taco.klkl.domain.region.dto.response.RegionResponseDto;
import taco.klkl.domain.region.exception.RegionNotFoundException;

@ExtendWith(MockitoExtension.class)
class RegionServiceImplTest {

	@InjectMocks
	RegionServiceImpl regionService;

	@Mock
	RegionRepository regionRepository;

	private static final Region region1 = Region.of("동북아시아");
	private static final Region region2 = Region.of("동남아시아");
	private static final Region region3 = Region.of("기타");

	@Test
	@DisplayName("모든 지역 조회 성공 테스트")
	void getAllRegionTest() {
		// given
		List<Region> mockRegions = Arrays.asList(region1, region2, region3);

		when(regionRepository.findAllByOrderByRegionIdAsc()).thenReturn(mockRegions);

		// when
		List<RegionResponseDto> regionResponseDtos = regionService.getAllRegions();

		// then
		assertThat(regionResponseDtos.size()).isEqualTo(3);
		assertThat(regionResponseDtos.get(0).name()).isEqualTo(region1.getName());
		assertThat(regionResponseDtos.get(1).name()).isEqualTo(region2.getName());
		assertThat(regionResponseDtos.get(2).name()).isEqualTo(region3.getName());
	}

	@Test
	@DisplayName("모든 지역 조회 실패 테스트")
	void getAllRegionFailTest() {
		// given
		when(regionRepository.findAllByOrderByRegionIdAsc()).thenReturn(Collections.emptyList());

		// when
		List<RegionResponseDto> regionResponseDtos = regionService.getAllRegions();

		// then
		assertThat(regionResponseDtos.size()).isEqualTo(0);
	}

	@Test
	@DisplayName("Id 지역 조회 성공 테스트")
	void getRegionById() {
		// given
		when(regionRepository.findById(1L)).thenReturn(Optional.of(region1));
		when(regionRepository.findById(2L)).thenReturn(Optional.of(region2));

		// when
		RegionResponseDto region1ResponseDto = regionService.getRegionById(1L);
		RegionResponseDto region2ResponseDto = regionService.getRegionById(2L);

		// then
		assertThat(region1ResponseDto.name()).isEqualTo(region1.getName());
		assertThat(region2ResponseDto.name()).isEqualTo(region2.getName());
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
		RegionResponseDto region1ResponseDto = regionService.getRegionByName(region1.getName());
		RegionResponseDto region2ResponseDto = regionService.getRegionByName(region2.getName());

		// then
		assertThat(region1ResponseDto.name()).isEqualTo(region1.getName());
		assertThat(region2ResponseDto.name()).isEqualTo(region2.getName());
	}

	@Test
	@DisplayName("Name 지역 조회 실패 테스트")
	void getRegionByNameFailTest() {
		// given
		when(regionRepository.findFirstByName(region1.getName())).thenThrow(new RegionNotFoundException());

		// when & then
		Assertions.assertThrows(RegionNotFoundException.class, () -> {
			regionService.getRegionByName(region1.getName());
		});

		verify(regionRepository, times(1)).findFirstByName(region1.getName());
	}
}
