package taco.klkl.domain.region.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import taco.klkl.domain.region.domain.City;

public interface CityRepository extends JpaRepository<City, Long> {
}
