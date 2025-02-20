package taco.klkl.domain.region.service.country;

import java.util.List;

import taco.klkl.domain.region.dto.response.country.CountryResponse;
import taco.klkl.domain.region.dto.response.country.CountrySimpleResponse;

public interface CountryService {

	List<CountrySimpleResponse> findAllCountriesByPartialString(final String partialString);

	CountryResponse getCountryById(final Long id);
}
