package taco.klkl.domain.region.service.country;

import java.util.List;

import org.springframework.stereotype.Service;

import taco.klkl.domain.region.dto.response.city.CityResponse;
import taco.klkl.domain.region.dto.response.country.CountryResponse;
import taco.klkl.domain.region.dto.response.country.CountrySimpleResponse;

@Service
public interface CountryService {
	List<CountryResponse> findAllCountries();

	CountryResponse findCountryById(final Long countryId);

	List<CityResponse> findCitiesByCountryId(final Long countryId);

	List<CountrySimpleResponse> findAllCountriesByPartialString(final String partialString);
}
