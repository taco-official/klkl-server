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
import taco.klkl.domain.region.domain.Currency;
import taco.klkl.domain.region.domain.Region;
import taco.klkl.domain.region.dto.response.CountryResponse;
import taco.klkl.domain.region.dto.response.RegionResponse;
import taco.klkl.domain.region.domain.CountryType;
import taco.klkl.domain.region.domain.CurrencyType;
import taco.klkl.domain.region.domain.RegionType;
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
	private final Currency currency1 = Currency.of(CurrencyType.JAPANESE_YEN, "flag");
	private final Country country1 = Country.of(
		CountryType.JAPAN,
		region1,
		"image/japan",
		"image/japan",
		currency1);
	private final Country country2 = Country.of(
		CountryType.TAIWAN,
		region1,
		"image/taiwan",
		"image/taiwan",
		currency1);
	private final List<Country> countryList = Arrays.asList(country1,
		country2);

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
		assertThat(regionResponses.get(0).name()).isEqualTo(region1.getName().getKoreanName());
		assertThat(regionResponses.get(1).name()).isEqualTo(region2.getName().getKoreanName());
		assertThat(regionResponses.get(2).name()).isEqualTo(region3.getName().getKoreanName());
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

	@Test
	@DisplayName("Id 지역 조회 성공 테스트")
	void testFindRegionById() {
		// given
		when(regionRepository.findById(1L)).thenReturn(Optional.of(region1));
		when(regionRepository.findById(2L)).thenReturn(Optional.of(region2));

		// when
		RegionResponse region1ResponseDto = regionService.findRegionById(1L);
		RegionResponse region2ResponseDto = regionService.findRegionById(2L);

		// then
		assertThat(region1ResponseDto.name()).isEqualTo(region1.getName().getKoreanName());
		assertThat(region2ResponseDto.name()).isEqualTo(region2.getName().getKoreanName());
	}

	@Test
	@DisplayName("지역에 있는 국가목록 조회")
	void testGetRegionWithCountry() {
		// given
		Region mockRegion = mock(Region.class);
		when(regionRepository.findById(1L)).thenReturn(Optional.of(mockRegion));
		when(mockRegion.getCountries()).thenReturn(countryList);

		// when
		List<CountryResponse> countriesDto = regionService.findCountriesByRegionId(1L);

		// then
		assertThat(countriesDto.size()).isEqualTo(2);
		assertThat(countriesDto.get(0)).isEqualTo(CountryResponse.from(country1));
		assertThat(countriesDto.get(1)).isEqualTo(CountryResponse.from(country2));
	}

	@Test
	@DisplayName("Id 지역 조회 실패 테스트")
	void testFindRegionByIdFail() {
		// given
		when(regionRepository.findById(1L)).thenThrow(new RegionNotFoundException());

		// when & then
		Assertions.assertThrows(RegionNotFoundException.class, () -> {
			regionService.findRegionById(1L);
		});

		verify(regionRepository, times(1)).findById(1L);
	}

	@Test
	@DisplayName("Name 지역 조회 성공 테스트")
	void testFindRegionByName() {
		// given
		when(regionRepository.findFirstByName(region1.getName())).thenReturn(region1);
		when(regionRepository.findFirstByName(region2.getName())).thenReturn(region2);

		// when
		RegionResponse region1ResponseDto = regionService.findRegionByName(region1.getName().getKoreanName());
		RegionResponse region2ResponseDto = regionService.findRegionByName(region2.getName().getKoreanName());

		// then
		assertThat(region1ResponseDto.name()).isEqualTo(region1.getName().getKoreanName());
		assertThat(region2ResponseDto.name()).isEqualTo(region2.getName().getKoreanName());
	}

	@Test
	@DisplayName("Name 지역 조회 실패 테스트")
	void testFindRegionByNameFail() {
		// given
		when(regionRepository.findFirstByName(region1.getName())).thenThrow(new RegionNotFoundException());

		// when & then
		Assertions.assertThrows(RegionNotFoundException.class, () -> {
			regionService.findRegionByName(region1.getName().getKoreanName());
		});

		verify(regionRepository, times(1)).findFirstByName(region1.getName());
	}
}
