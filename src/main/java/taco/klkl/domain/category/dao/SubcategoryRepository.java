package taco.klkl.domain.category.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import taco.klkl.domain.category.domain.Subcategory;
import taco.klkl.domain.category.domain.SubcategoryName;

@Repository
public interface SubcategoryRepository extends JpaRepository<Subcategory, Long> {
	List<Subcategory> findAllByNameIn(final List<SubcategoryName> names);
}
