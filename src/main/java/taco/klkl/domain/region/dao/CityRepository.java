package taco.klkl.domain.region.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import taco.klkl.domain.region.domain.City;
import taco.klkl.domain.region.domain.CityType;

public interface CityRepository extends JpaRepository<City, Long> {
	List<City> findAllByNameIn(final List<CityType> names);
}
