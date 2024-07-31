package taco.klkl.domain.region.service;

import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import taco.klkl.domain.region.dao.CountryRepository;
import taco.klkl.domain.region.domain.Country;
import taco.klkl.domain.region.dto.response.CountryResponseDto;
import taco.klkl.domain.region.enums.CountryType;
import taco.klkl.domain.region.exception.CountryNotFoundException;

@Slf4j
@Primary
@Service
@Transactional
@RequiredArgsConstructor
public class CountryServiceImpl implements CountryService {

	private final CountryRepository countryRepository;

	@Override
	public List<CountryResponseDto> getALlCountry() {
		List<Country> countries = countryRepository.findAll();

		if (countries.isEmpty()) {
			return Collections.emptyList();
		}

		return countries.stream()
			.map(CountryResponseDto::from)
			.toList();
	}

	@Override
	public CountryResponseDto getCountryById(Long countryId) {
		Country country = countryRepository.findById(countryId)
			.orElseThrow(CountryNotFoundException::new);

		return CountryResponseDto.from(country);
	}

	@Override
	public CountryResponseDto getCountryByName(CountryType name) {
		Country country = countryRepository.findFirstByName(name);

		if (country == null) {
			throw new CountryNotFoundException();
		}

		return CountryResponseDto.from(country);
	}
}
