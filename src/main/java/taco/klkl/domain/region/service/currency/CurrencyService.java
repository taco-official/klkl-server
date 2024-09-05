package taco.klkl.domain.region.service.currency;

import java.util.List;

import org.springframework.stereotype.Service;

import taco.klkl.domain.region.dto.response.currency.CurrencyResponse;

@Service
public interface CurrencyService {

	List<CurrencyResponse> findAllCurrencies();

	CurrencyResponse findCurrencyByCountryId(final Long countryId);
}
