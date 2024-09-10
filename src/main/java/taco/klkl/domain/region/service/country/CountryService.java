package taco.klkl.domain.region.service.country;

import java.util.List;

import org.springframework.stereotype.Service;

import taco.klkl.domain.region.dto.response.country.CountrySimpleResponse;

@Service
public interface CountryService {

	List<CountrySimpleResponse> findAllCountriesByPartialString(final String partialString);
}
