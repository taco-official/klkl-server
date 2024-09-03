package taco.klkl.domain.region.dao.city;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import taco.klkl.domain.region.domain.city.City;

public interface CityRepository extends JpaRepository<City, Long> {
	List<City> findAllByNameLike(final String partialName);
}
