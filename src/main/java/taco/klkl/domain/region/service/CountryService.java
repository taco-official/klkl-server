package taco.klkl.domain.region.service;

import java.util.List;

import org.springframework.stereotype.Service;

import taco.klkl.domain.region.dto.response.CityResponseDto;
import taco.klkl.domain.region.dto.response.CountryResponseDto;
import taco.klkl.domain.region.dto.response.CountrySimpleResponseDto;
import taco.klkl.domain.region.enums.CountryType;

@Service
public interface CountryService {
	List<CountryResponseDto> getAllCountries();

	CountryResponseDto getCountryById(final Long countryId);

	List<CityResponseDto> getCitiesByCountryId(final Long countryId);

	List<CountrySimpleResponseDto> getAllCountriesByCountryTypes(final List<CountryType> countryTypes);

	boolean existsCountryById(final Long countryId);
}
