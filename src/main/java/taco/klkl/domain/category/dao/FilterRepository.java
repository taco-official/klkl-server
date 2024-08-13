package taco.klkl.domain.category.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import taco.klkl.domain.category.domain.Filter;

@Repository
public interface FilterRepository extends JpaRepository<Filter, Long> {
}
