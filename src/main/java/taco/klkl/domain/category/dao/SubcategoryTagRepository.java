package taco.klkl.domain.category.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import taco.klkl.domain.category.domain.SubcategoryTag;

public interface SubcategoryTagRepository extends JpaRepository<SubcategoryTag, Long> {
}
