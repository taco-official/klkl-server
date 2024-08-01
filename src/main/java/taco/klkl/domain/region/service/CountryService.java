package taco.klkl.domain.region.service;

import java.util.List;

import org.springframework.stereotype.Service;

import taco.klkl.domain.region.dto.response.CountryResponseDto;
import taco.klkl.domain.region.dto.response.CountryWithCitiesResponseDto;

@Service
public interface CountryService {
	List<CountryResponseDto> getAllCountries();

	CountryResponseDto getCountryById(Long countryId);

	CountryWithCitiesResponseDto getCountryWithCitiesById(Long countryId);
}
