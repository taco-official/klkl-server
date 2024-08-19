package taco.klkl.domain.category.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import taco.klkl.domain.category.domain.Subcategory;
import taco.klkl.domain.category.domain.SubcategoryTag;

@Repository
public interface SubcategoryTagRepository extends JpaRepository<SubcategoryTag, Long> {
	List<SubcategoryTag> findAllBySubcategory(final Subcategory subcategory);
}
