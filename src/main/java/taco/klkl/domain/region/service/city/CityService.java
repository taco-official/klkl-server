package taco.klkl.domain.region.service.city;

import java.util.List;

import taco.klkl.domain.region.dto.response.city.CityHierarchyResponse;
import taco.klkl.domain.region.dto.response.city.CitySimpleResponse;

public interface CityService {

	List<CitySimpleResponse> findAllCitiesByPartialString(final String partialString);

	CityHierarchyResponse findCityHierarchyById(final Long id);
}
