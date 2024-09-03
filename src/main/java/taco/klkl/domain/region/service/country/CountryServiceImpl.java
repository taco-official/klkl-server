package taco.klkl.domain.region.service.country;

import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import taco.klkl.domain.region.dao.country.CountryRepository;
import taco.klkl.domain.region.domain.country.Country;
import taco.klkl.domain.region.dto.response.city.CityResponse;
import taco.klkl.domain.region.dto.response.country.CountryResponse;
import taco.klkl.domain.region.dto.response.country.CountrySimpleResponse;
import taco.klkl.domain.region.exception.country.CountryNotFoundException;

@Slf4j
@Primary
@Service
@Transactional
@RequiredArgsConstructor
public class CountryServiceImpl implements CountryService {

	private final CountryRepository countryRepository;

	@Override
	public List<CountryResponse> findAllCountries() {
		final List<Country> countries = countryRepository.findAll();
		if (countries.isEmpty()) {
			return Collections.emptyList();
		}
		return countries.stream()
			.map(CountryResponse::from)
			.toList();
	}

	@Override
	public CountryResponse findCountryById(final Long countryId) throws CountryNotFoundException {
		final Country country = countryRepository.findById(countryId)
			.orElseThrow(CountryNotFoundException::new);
		return CountryResponse.from(country);
	}

	@Override
	public List<CityResponse> findCitiesByCountryId(final Long countryId) throws CountryNotFoundException {
		final Country country = countryRepository.findById(countryId)
			.orElseThrow(CountryNotFoundException::new);
		return country.getCities().stream()
			.map(CityResponse::from)
			.toList();
	}

	@Override
	public List<CountrySimpleResponse> findAllCountriesByPartialString(final String partialString) {
		if (partialString == null || partialString.isEmpty()) {
			return List.of();
		}
		final List<Country> countries = countryRepository.findAllByNameLike(partialString);
		return countries.stream()
			.map(CountrySimpleResponse::from)
			.toList();
	}
}
