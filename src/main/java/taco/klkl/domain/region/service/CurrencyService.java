package taco.klkl.domain.region.service;

import java.util.List;

import org.springframework.stereotype.Service;

import taco.klkl.domain.region.dto.response.CurrencyResponseDto;

@Service
public interface CurrencyService {
	List<CurrencyResponseDto> getAllCurrencies();

	CurrencyResponseDto getCurrencyById(final Long id);
}
