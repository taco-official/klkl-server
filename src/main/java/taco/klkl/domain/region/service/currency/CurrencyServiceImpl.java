package taco.klkl.domain.region.service.currency;

import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import taco.klkl.domain.region.dao.currency.CurrencyRepository;
import taco.klkl.domain.region.domain.country.Country;
import taco.klkl.domain.region.domain.currency.Currency;
import taco.klkl.domain.region.dto.response.currency.CurrencyResponse;
import taco.klkl.global.util.CountryUtil;

@Slf4j
@Primary
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CurrencyServiceImpl implements CurrencyService {

	private final CurrencyRepository currencyRepository;
	private final CountryUtil countryUtil;

	@Override
	public List<CurrencyResponse> findAllCurrencies() {

		final List<Currency> findCurrencies = currencyRepository.findAll();

		if (findCurrencies.isEmpty()) {
			return Collections.emptyList();
		}

		return findCurrencies.stream()
			.map(CurrencyResponse::from)
			.toList();
	}

	@Override
	public CurrencyResponse findCurrencyByCountryId(final Long countryId) {
		final Country country = countryUtil.findCountryEntityById(countryId);
		return CurrencyResponse.from(country.getCurrency());
	}
}
