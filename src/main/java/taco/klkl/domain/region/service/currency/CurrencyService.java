package taco.klkl.domain.region.service.currency;

import java.util.List;

import taco.klkl.domain.region.dto.response.currency.CurrencyResponse;

public interface CurrencyService {

	List<CurrencyResponse> findAllCurrencies();

	CurrencyResponse findCurrencyByCountryId(final Long countryId);
}
