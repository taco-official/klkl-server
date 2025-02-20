package taco.klkl.global.util;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import taco.klkl.domain.region.dao.currency.CurrencyRepository;
import taco.klkl.domain.region.domain.currency.Currency;
import taco.klkl.domain.region.exception.currency.CurrencyNotFoundException;

@Component
@RequiredArgsConstructor
public class CurrencyUtil {

	private final CurrencyRepository currencyRepository;

	public Currency findCurrencyEntityById(final Long id) {
		return currencyRepository.findById(id)
			.orElseThrow(CurrencyNotFoundException::new);
	}
}
