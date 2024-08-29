package taco.klkl.domain.region.service;

import java.util.List;

import org.springframework.stereotype.Service;

import taco.klkl.domain.region.dto.response.CityResponse;

@Service
public interface CityService {

	List<CityResponse> findAllCitiesByPartialString(final String partialString);

}
