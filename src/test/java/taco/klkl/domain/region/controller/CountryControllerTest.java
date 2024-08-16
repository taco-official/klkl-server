package taco.klkl.domain.region.controller;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import taco.klkl.domain.region.dao.CountryRepository;
import taco.klkl.domain.region.domain.City;
import taco.klkl.domain.region.domain.Country;
import taco.klkl.domain.region.domain.Currency;
import taco.klkl.domain.region.domain.Region;
import taco.klkl.domain.region.dto.response.CityResponse;
import taco.klkl.domain.region.dto.response.CountryResponse;
import taco.klkl.domain.region.domain.CityType;
import taco.klkl.domain.region.domain.CountryType;
import taco.klkl.domain.region.domain.CurrencyType;
import taco.klkl.domain.region.domain.RegionType;
import taco.klkl.domain.region.service.CountryService;

@WebMvcTest(CountryController.class)
public class CountryControllerTest {

	@Autowired
	MockMvc mockMvc;

	@MockBean
	CountryService countryService;

	@MockBean
	private CountryRepository countryRepository;

	private final Region region = Region.of(RegionType.NORTHEAST_ASIA);
	private final Currency currency1 = Currency.of(CurrencyType.JAPANESE_YEN, "flag");
	private final Country country1 = Country.of(
		CountryType.JAPAN,
		region,
		"test",
		"test",
		currency1);
	private final Country country2 = Country.of(
		CountryType.TAIWAN,
		region,
		"test",
		"test",
		currency1);
	private final City city1 = City.of(country1, CityType.OSAKA);
	private final City city2 = City.of(country1, CityType.KYOTO);
	private final List<City> cities = Arrays.asList(city1, city2);

	@Test
	@DisplayName("모든 국가 조회 테스트")
	void testFindAllCountries() throws Exception {
		// given
		List<CountryResponse> countryResponses = Arrays.asList(
			CountryResponse.from(country1),
			CountryResponse.from(country2)
		);

		when(countryService.findAllCountries()).thenReturn(countryResponses);

		// when & then
		mockMvc.perform(get("/v1/countries")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.isSuccess", is(true)))
			.andExpect(jsonPath("$.code", is("C000")))
			.andExpect(jsonPath("$.data", hasSize(2)))
			.andExpect(jsonPath("$.data[0].name", is(country1.getName().getKoreanName())))
			.andExpect(jsonPath("$.data[1].name", is(country2.getName().getKoreanName())))
			.andExpect(jsonPath("$.data[0].currency.code", is(country1.getCurrency().getCode().getCodeName())))
			.andExpect(jsonPath("$.timestamp", notNullValue()));

		verify(countryService, times(1)).findAllCountries();
	}

	@Test
	@DisplayName("Id로 국가 조회 테스트")
	void testFindCountryById() throws Exception {
		// given
		CountryResponse countryResponse = CountryResponse.from(country1);

		when(countryService.findCountryById(400L)).thenReturn(countryResponse);

		// when & then
		mockMvc.perform(get("/v1/countries/400")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.isSuccess", is(true)))
			.andExpect(jsonPath("$.code", is("C000")))
			.andExpect(jsonPath("$.data.name", is(country1.getName().getKoreanName())))
			.andExpect(jsonPath("$.timestamp", notNullValue()));

		verify(countryService, times(1)).findCountryById(400L);
	}

	@Test
	@DisplayName("국가와 도시 조회")
	void testGetCountryWithCities() throws Exception {
		// given
		Country mockCountry = mock(Country.class);
		CountryRepository mockCountryRepository = mock(CountryRepository.class);
		when(mockCountry.getName()).thenReturn(CountryType.JAPAN);
		when(countryRepository.findById(400L)).thenReturn(Optional.of(mockCountry));
		when(mockCountry.getCities()).thenReturn(cities);
		when(countryService.findCitiesByCountryId(400L)).thenReturn(cities.stream().map(CityResponse::from).toList());

		// when & then
		mockMvc.perform(get("/v1/countries/400/cities")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.isSuccess", is(true)))
			.andExpect(jsonPath("$.code", is("C000")))
			.andExpect(jsonPath("$.data[0].name", is(cities.get(0).getName().getKoreanName())))
			.andExpect(jsonPath("$.data[1].name", is(cities.get(1).getName().getKoreanName())))
			.andExpect(jsonPath("$.timestamp", notNullValue()));

		verify(countryService, times(1)).findCitiesByCountryId(400L);
	}

}
