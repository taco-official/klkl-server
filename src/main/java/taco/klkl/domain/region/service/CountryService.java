package taco.klkl.domain.region.service;

import java.util.List;

import org.springframework.stereotype.Service;

import taco.klkl.domain.region.dto.response.CityResponse;
import taco.klkl.domain.region.dto.response.CountryResponse;
import taco.klkl.domain.region.dto.response.CountrySimpleResponse;
import taco.klkl.domain.region.enums.CountryType;

@Service
public interface CountryService {
	List<CountryResponse> getAllCountries();

	CountryResponse getCountryById(final Long countryId);

	List<CityResponse> getCitiesByCountryId(final Long countryId);

	List<CountrySimpleResponse> getAllCountriesByCountryTypes(final List<CountryType> countryTypes);
}
