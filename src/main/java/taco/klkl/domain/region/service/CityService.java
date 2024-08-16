package taco.klkl.domain.region.service;

import java.util.List;

import org.springframework.stereotype.Service;

import taco.klkl.domain.region.dto.response.CityResponse;
import taco.klkl.domain.region.domain.CityType;

@Service
public interface CityService {

	List<CityResponse> getAllCitiesByCityTypes(final List<CityType> cityTypes);

}
