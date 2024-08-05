package taco.klkl.domain.category.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import taco.klkl.domain.category.domain.Filter;

public interface SubcategoryFilterRepository extends JpaRepository<Filter, Long> {
}
