package taco.klkl.domain.region.integration;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import jakarta.transaction.Transactional;
import taco.klkl.domain.region.dto.response.CountryResponseDto;
import taco.klkl.domain.region.service.CountryService;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class CountryIntegrationTest {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	CountryService countryService;

	@Test
	@DisplayName("모든 국가 조회 테스트")
	void getAllCountriesTest() throws Exception {
		// given
		List<CountryResponseDto> countryResponseDtos = countryService.getAllCountries();

		// when & then
		mockMvc.perform(get("/v1/countries")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code", is("C000")))
			.andExpect(jsonPath("$.isSuccess", is(true)))
			.andExpect(jsonPath("$.data", hasSize(countryResponseDtos.size())));
	}

	@Test
	@DisplayName("id로 국가 조회 테스트")
	void getCountryByIdTest() throws Exception {
		// given
		CountryResponseDto countryResponseDto = countryService.getCountryById(403L);

		// when & then
		mockMvc.perform(get("/v1/countries/403")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code", is("C000")))
			.andExpect(jsonPath("$.isSuccess", is(true)))
			.andExpect(jsonPath("$.data.name", is(countryResponseDto.name())));
	}
}
