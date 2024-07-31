package taco.klkl.domain.region.controller;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import taco.klkl.domain.region.domain.Country;
import taco.klkl.domain.region.domain.Region;
import taco.klkl.domain.region.dto.response.CountryResponseDto;
import taco.klkl.domain.region.enums.CountryType;
import taco.klkl.domain.region.enums.RegionType;
import taco.klkl.domain.region.service.CountryService;

@WebMvcTest(CountryController.class)
public class CountryControllerTest {

	@Autowired
	MockMvc mockMvc;

	@MockBean
	CountryService countryService;

	private final Region region = Region.of(RegionType.NORTHEAST_ASIA);
	private final Country country1 = Country.of(
		CountryType.JAPAN,
		region,
		"test",
		"test",
		1);
	private final Country country2 = Country.of(
		CountryType.TAIWAN,
		region,
		"test",
		"test",
		1);

	@Test
	@DisplayName("모든 국가 조회 테스트")
	void getAllCountriesTest() throws Exception {
		// given
		List<CountryResponseDto> countryResponseDtos = Arrays.asList(
			CountryResponseDto.from(country1),
			CountryResponseDto.from(country2)
		);

		when(countryService.getALlCountry()).thenReturn(countryResponseDtos);

		// when & then
		mockMvc.perform(get("/v1/countries")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.isSuccess", is(true)))
			.andExpect(jsonPath("$.code", is("C000")))
			.andExpect(jsonPath("$.data", hasSize(2)))
			.andExpect(jsonPath("$.data[0].name", is(country1.getName().getKoreanName())))
			.andExpect(jsonPath("$.data[1].name", is(country2.getName().getKoreanName())))
			.andExpect(jsonPath("$.timestamp", notNullValue()));

		verify(countryService, times(1)).getALlCountry();
	}

	@Test
	@DisplayName("Id로 국가 조회 테스트")
	void getCountryByIdTest() throws Exception {
		// given
		CountryResponseDto countryResponseDto = CountryResponseDto.from(country1);

		when(countryService.getCountryById(400L)).thenReturn(countryResponseDto);

		// when & then
		mockMvc.perform(get("/v1/countries/400")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.isSuccess", is(true)))
			.andExpect(jsonPath("$.code", is("C000")))
			.andExpect(jsonPath("$.data.name", is(country1.getName().getKoreanName())))
			.andExpect(jsonPath("$.timestamp", notNullValue()));

		verify(countryService, times(1)).getCountryById(400L);
	}

}
