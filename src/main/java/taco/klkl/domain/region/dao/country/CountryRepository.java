package taco.klkl.domain.region.dao.country;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import taco.klkl.domain.region.domain.country.Country;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {
	List<Country> findAllByNameContaining(final String partialName);
}
