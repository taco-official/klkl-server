package taco.klkl.global.util;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import taco.klkl.domain.region.dao.CurrencyRepository;
import taco.klkl.domain.region.domain.Currency;
import taco.klkl.domain.region.exception.CurrencyNotFoundException;

@Component
@RequiredArgsConstructor
public class CurrencyUtil {

	private final CurrencyRepository currencyRepository;

	public Currency getCurrencyEntityById(final Long id) {

		return currencyRepository.findById(id)
			.orElseThrow(CurrencyNotFoundException::new);
	}
}
