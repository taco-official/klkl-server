package taco.klkl.domain.region.service;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import taco.klkl.domain.region.dto.response.CityResponse;
import taco.klkl.domain.region.enums.CityType;

@Service
public interface CityService {

	List<CityResponse> getAllCitiesByCityTypes(final List<CityType> cityTypes);

}
