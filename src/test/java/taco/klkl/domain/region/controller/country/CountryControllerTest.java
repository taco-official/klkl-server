package taco.klkl.domain.region.controller.country;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import taco.klkl.domain.region.dao.country.CountryRepository;
import taco.klkl.domain.region.domain.country.Country;
import taco.klkl.domain.region.domain.country.CountryType;
import taco.klkl.domain.region.domain.currency.Currency;
import taco.klkl.domain.region.domain.currency.CurrencyType;
import taco.klkl.domain.region.domain.region.Region;
import taco.klkl.domain.region.domain.region.RegionType;
import taco.klkl.domain.region.dto.response.country.CountryResponse;
import taco.klkl.domain.region.service.country.CountryService;
import taco.klkl.domain.token.service.TokenProvider;
import taco.klkl.global.config.security.TestSecurityConfig;
import taco.klkl.global.util.ResponseUtil;

@WebMvcTest(CountryController.class)
@Import(TestSecurityConfig.class)
public class CountryControllerTest {

	@Autowired
	MockMvc mockMvc;

	@MockBean
	private TokenProvider tokenProvider;

	@MockBean
	private ResponseUtil responseUtil;

	@MockBean
	CountryService countryService;

	@MockBean
	private CountryRepository countryRepository;

	private final Region region = Region.from(RegionType.NORTHEAST_ASIA);
	private final Currency currency1 = Currency.of(CurrencyType.JAPANESE_YEN);
	private final Country country1 = Country.of(
		CountryType.JAPAN,
		region,
		"wallpaper",
		currency1);

	@Test
	@DisplayName("Id로 국가 조회 테스트")
	void testFindCountryById() throws Exception {
		// given
		CountryResponse countryResponse = CountryResponse.from(country1);

		when(countryService.getCountryById(400L)).thenReturn(countryResponse);

		// when & then
		mockMvc.perform(get("/v1/countries/400")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.isSuccess", is(true)))
			.andExpect(jsonPath("$.data.name", is(country1.getName())))
			.andExpect(jsonPath("$.timestamp", notNullValue()));

		verify(countryService, times(1)).getCountryById(400L);
	}

}
