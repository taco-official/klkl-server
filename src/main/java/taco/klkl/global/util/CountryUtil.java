package taco.klkl.global.util;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import taco.klkl.domain.region.dao.country.CountryRepository;
import taco.klkl.domain.region.domain.country.Country;
import taco.klkl.domain.region.exception.country.CountryNotFoundException;

@Component
@RequiredArgsConstructor
public class CountryUtil {

	private final CountryRepository countryRepository;

	public Country findCountryEntityById(final Long id) {
		return countryRepository.findById(id)
			.orElseThrow(CountryNotFoundException::new);
	}
}
