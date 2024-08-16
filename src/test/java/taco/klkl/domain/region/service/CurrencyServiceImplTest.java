package taco.klkl.domain.region.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import taco.klkl.domain.region.dao.CurrencyRepository;
import taco.klkl.domain.region.domain.Currency;
import taco.klkl.domain.region.domain.CurrencyType;
import taco.klkl.domain.region.dto.response.CurrencyResponse;

@ExtendWith(MockitoExtension.class)
public class CurrencyServiceImplTest {

	@InjectMocks
	CurrencyServiceImpl currencyService;

	@Mock
	CurrencyRepository currencyRepository;

	private final Currency currency1 = Currency.of(CurrencyType.JAPANESE_YEN, "test1");
	private final Currency currency2 = Currency.of(CurrencyType.THAI_BAHT, "test2");
	private final CurrencyResponse currencyResponse1 = CurrencyResponse.from(currency1);
	private final CurrencyResponse currencyResponse2 = CurrencyResponse.from(currency2);

	@Test
	@DisplayName("모든 통화 목록 조회 테스트")
	void testGetAllCurrency() {
		// given
		when(currencyRepository.findAll()).thenReturn(Arrays.asList(currency1, currency2));

		// when
		List<CurrencyResponse> currencyResponses = currencyService.findAllCurrencies();

		// then
		assertThat(currencyResponses.get(0)).isEqualTo(currencyResponse1);
		assertThat(currencyResponses.get(1)).isEqualTo(currencyResponse2);
	}
}
