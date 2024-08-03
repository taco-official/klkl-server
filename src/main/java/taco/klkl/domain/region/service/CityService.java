package taco.klkl.domain.region.service;

import org.springframework.stereotype.Service;

@Service
public interface CityService {
	boolean existsCityById(final Long id);
}
