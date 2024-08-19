package taco.klkl.domain.region.controller;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import taco.klkl.domain.region.dao.RegionRepository;
import taco.klkl.domain.region.domain.Country;
import taco.klkl.domain.region.domain.CountryType;
import taco.klkl.domain.region.domain.Currency;
import taco.klkl.domain.region.domain.CurrencyType;
import taco.klkl.domain.region.domain.Region;
import taco.klkl.domain.region.domain.RegionType;
import taco.klkl.domain.region.dto.response.CountryResponse;
import taco.klkl.domain.region.dto.response.RegionResponse;
import taco.klkl.domain.region.service.RegionService;
import taco.klkl.global.error.exception.ErrorCode;

@WebMvcTest(RegionController.class)
class RegionControllerTest {

	@Autowired
	MockMvc mockMvc;

	@MockBean
	RegionService regionService;

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
	void testFindAllRegions() throws Exception {
		// given
		List<RegionResponse> regionResponses = Arrays.asList(
			RegionResponse.from(region1),
			RegionResponse.from(region2),
			RegionResponse.from(region3)
		);

		when(regionService.findAllRegions()).thenReturn(regionResponses);

		// when & then
		mockMvc.perform(get("/v1/regions")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.isSuccess", is(true)))
			.andExpect(jsonPath("$.data", hasSize(3)))
			.andExpect(jsonPath("$.data[0].name", is(region1.getName().getKoreanName())))
			.andExpect(jsonPath("$.data[1].name", is(region2.getName().getKoreanName())))
			.andExpect(jsonPath("$.data[2].name", is(region3.getName().getKoreanName())))
			.andExpect(jsonPath("$.timestamp", notNullValue()));

		verify(regionService, times(1)).findAllRegions();
	}

	@Test
	@DisplayName("모든 지역 조회 empty 테스트")
	void testFindAllRegionsEmpty() throws Exception {
		// given
		when(regionService.findAllRegions()).thenReturn(Collections.emptyList());

		// when & then
		mockMvc.perform(get("/v1/regions")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.isSuccess", is(true)))
			.andExpect(jsonPath("$.data", hasSize(0)))
			.andExpect(jsonPath("$.timestamp", notNullValue()));

		verify(regionService, times(1)).findAllRegions();
	}

	@Test
	@DisplayName("모든 지역 조회 실패 테스트")
	void testGetRegionsFail() throws Exception {
		// given
		when(regionService.findAllRegions()).thenThrow(RuntimeException.class);

		// when & then
		mockMvc.perform(get("/v1/regions")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().is5xxServerError())
			.andExpect(jsonPath("$.isSuccess", is(false)))
			.andExpect(jsonPath("$.status", is(ErrorCode.INTERNAL_SERVER_ERROR.getHttpStatus().value())))
			.andExpect(jsonPath("$.data.message", is(ErrorCode.INTERNAL_SERVER_ERROR.getMessage())));

		verify(regionService, times(1)).findAllRegions();
	}

	@Test
	@DisplayName("특정 지역에 속한 국가 목록 조회")
	void testGetRegionWithCountry() throws Exception {
		// given
		Region mockRegion = mock(Region.class);
		RegionRepository regionRepository = mock(RegionRepository.class);
		when(mockRegion.getName()).thenReturn(RegionType.NORTHEAST_ASIA);
		when(regionRepository.findById(1L)).thenReturn(Optional.of(mockRegion));
		when(mockRegion.getCountries()).thenReturn(countryList);
		List<CountryResponse> responseDto = countryList.stream().map(CountryResponse::from).toList();
		when(regionService.findCountriesByRegionId(1L)).thenReturn(responseDto);

		// when & then
		mockMvc.perform(get("/v1/regions/1/countries")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.isSuccess", is(true)))
			.andExpect(jsonPath("$.data[0].name", is(countryList.get(0).getName().getKoreanName())))
			.andExpect(jsonPath("$.data[1].name", is(countryList.get(1).getName().getKoreanName())))
			.andExpect(jsonPath("$.timestamp", notNullValue()));

		verify(regionService, times(1)).findCountriesByRegionId(1L);
	}
}
