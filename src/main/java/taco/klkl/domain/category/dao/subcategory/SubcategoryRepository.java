package taco.klkl.domain.category.dao.subcategory;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import taco.klkl.domain.category.domain.subcategory.Subcategory;

@Repository
public interface SubcategoryRepository extends JpaRepository<Subcategory, Long> {
	List<Subcategory> findAllByNameContaining(final String partialName);
}
