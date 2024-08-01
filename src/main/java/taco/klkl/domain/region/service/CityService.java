package taco.klkl.domain.region.service;

import org.springframework.stereotype.Service;

@Service
public interface CityService {
	boolean exitsCityById(final Long id);
}
