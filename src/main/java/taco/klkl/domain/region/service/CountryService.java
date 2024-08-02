package taco.klkl.domain.region.service;

import java.util.List;

import org.springframework.stereotype.Service;

import taco.klkl.domain.region.dto.response.CityResponseDto;
import taco.klkl.domain.region.dto.response.CountryResponseDto;

@Service
public interface CountryService {
	List<CountryResponseDto> getAllCountries();

	CountryResponseDto getCountryById(final Long countryId);

	List<CityResponseDto> getCitiesByCountryId(final Long countryId);
}
