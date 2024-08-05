package taco.klkl.domain.region.service;

import org.springframework.stereotype.Service;

import taco.klkl.domain.region.domain.City;

@Service
public interface CityService {
	City getCityById(Long id);
}
