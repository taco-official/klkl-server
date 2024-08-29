package taco.klkl.domain.region.service;

import java.util.List;

import org.springframework.stereotype.Service;

import taco.klkl.domain.region.dto.response.CityResponse;
import taco.klkl.domain.region.dto.response.CountryResponse;
import taco.klkl.domain.region.dto.response.CountrySimpleResponse;

@Service
public interface CountryService {
	List<CountryResponse> findAllCountries();

	CountryResponse findCountryById(final Long countryId);

	List<CityResponse> findCitiesByCountryId(final Long countryId);

	List<CountrySimpleResponse> findAllCountriesByPartialString(final String partialString);
}
