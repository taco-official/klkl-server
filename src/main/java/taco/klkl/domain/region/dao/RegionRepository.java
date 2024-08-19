package taco.klkl.domain.region.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import taco.klkl.domain.region.domain.Region;
import taco.klkl.domain.region.domain.RegionType;

@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {
	Region findFirstByName(final RegionType name);

	List<Region> findAllByOrderByIdAsc();
}
