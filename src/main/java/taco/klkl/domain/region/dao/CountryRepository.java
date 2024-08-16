package taco.klkl.domain.region.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import taco.klkl.domain.region.domain.Country;
import taco.klkl.domain.region.domain.CountryType;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {
	List<Country> findAllByNameIn(final List<CountryType> names);
}
