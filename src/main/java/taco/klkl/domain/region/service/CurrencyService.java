package taco.klkl.domain.region.service;

import java.util.List;

import org.springframework.stereotype.Service;

import taco.klkl.domain.region.domain.Currency;
import taco.klkl.domain.region.dto.response.CurrencyResponse;

@Service
public interface CurrencyService {
	List<CurrencyResponse> getAllCurrencies();

	Currency getCurrencyEntityById(final Long id);
}
