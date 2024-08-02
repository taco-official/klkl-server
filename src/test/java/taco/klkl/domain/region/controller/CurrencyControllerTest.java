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

import taco.klkl.domain.region.dao.CurrencyRepository;
import taco.klkl.domain.region.domain.Currency;
import taco.klkl.domain.region.dto.response.CurrencyResponseDto;
import taco.klkl.domain.region.enums.CurrencyType;
import taco.klkl.domain.region.service.CurrencyService;

@WebMvcTest(CurrencyController.class)
public class CurrencyControllerTest {

	@Autowired
	CurrencyController currencyController;

	@MockBean
	CurrencyService currencyService;

	@MockBean
	CurrencyRepository currencyRepository;

	private final Currency currency1 = Currency.of(CurrencyType.YEN, "test1");
	private final Currency currency2 = Currency.of(CurrencyType.BAHT, "test2");
	private final CurrencyResponseDto currencyResponseDto1 = CurrencyResponseDto.from(currency1);
	private final CurrencyResponseDto currencyResponseDto2 = CurrencyResponseDto.from(currency2);
	@Autowired
	private MockMvc mockMvc;

	@Test
	@DisplayName("모든 통화 목록 조회")
	void getAllCurrencyTest() throws Exception {
		// given
		List<CurrencyResponseDto> currencyResponseDtoList = Arrays.asList(currencyResponseDto1, currencyResponseDto2);
		when(currencyService.getAllCurrencies()).thenReturn(currencyResponseDtoList);

		// when & then
		mockMvc.perform(get("/v1/currencies")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.isSuccess", is(true)))
			.andExpect(jsonPath("$.code", is("C000")))
			.andExpect(jsonPath("$.data", hasSize(2)))
			.andExpect(jsonPath("$.data[0].code", is(currency1.getCode().getCodeName())))
			.andExpect(jsonPath("$.data[1].code", is(currency2.getCode().getCodeName())))
			.andExpect(jsonPath("$.timestamp", notNullValue()));
	}
}
