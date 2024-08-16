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
import taco.klkl.domain.region.dto.response.CityResponse;
import taco.klkl.domain.region.dto.response.CountryResponse;
import taco.klkl.domain.region.dto.response.CountrySimpleResponse;
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
	public List<CountryResponse> getAllCountries() {

		final List<Country> countries = countryRepository.findAll();

		if (countries.isEmpty()) {
			return Collections.emptyList();
		}

		return countries.stream()
			.map(CountryResponse::from)
			.toList();
	}

	@Override
	public CountryResponse getCountryById(final Long countryId) throws CountryNotFoundException {

		final Country country = countryRepository.findById(countryId)
			.orElseThrow(CountryNotFoundException::new);

		return CountryResponse.from(country);
	}

	@Override
	public List<CityResponse> getCitiesByCountryId(final Long countryId) throws CountryNotFoundException {

		final Country country = countryRepository.findById(countryId)
			.orElseThrow(CountryNotFoundException::new);

		return country.getCities().stream()
			.map(CityResponse::from)
			.toList();
	}

	@Override
	public List<CountrySimpleResponse> getAllCountriesByCountryTypes(final List<CountryType> countryTypes) {

		if (countryTypes == null || countryTypes.isEmpty()) {
			return List.of();
		}

		final List<Country> findCountries = countryRepository.findAllByNameIn(countryTypes);

		return findCountries.stream()
			.map(CountrySimpleResponse::from)
			.toList();
	}

	@Override
	public boolean existsCountryById(final Long countryId) {
		return countryRepository.existsById(countryId);
	}
}
