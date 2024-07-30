package taco.klkl.domain.region.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import taco.klkl.domain.region.domain.Country;
import taco.klkl.domain.region.enums.CountryType;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {
	Country getFirstByName(CountryType name);

}
