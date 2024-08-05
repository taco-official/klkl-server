package taco.klkl.domain.region.service;

import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import taco.klkl.domain.region.dao.CurrencyRepository;
import taco.klkl.domain.region.domain.Currency;
import taco.klkl.domain.region.dto.response.CurrencyResponseDto;
import taco.klkl.domain.region.exception.CurrencyNotFoundException;

@Slf4j
@Primary
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CurrencyServiceImpl implements CurrencyService {

	private final CurrencyRepository currencyRepository;

	@Override
	public List<CurrencyResponseDto> getAllCurrencies() {

		final List<Currency> findCurrencies = currencyRepository.findAll();

		if (findCurrencies.isEmpty()) {
			return Collections.emptyList();
		}

		return findCurrencies.stream()
			.map(CurrencyResponseDto::from)
			.toList();
	}

	@Override
	public Currency getCurrencyById(final Long id) {

		return currencyRepository.findById(id)
			.orElseThrow(CurrencyNotFoundException::new);
	}
}
