package taco.klkl.domain.region.service;

import java.util.List;

import org.springframework.stereotype.Service;

import taco.klkl.domain.region.dto.response.CountryResponseDto;
import taco.klkl.domain.region.enums.CountryType;

@Service
public interface CountryService {
	List<CountryResponseDto> getALlCountry();

	CountryResponseDto getCountryById(Long countryId);

	CountryResponseDto getCountryByName(CountryType name);
}
