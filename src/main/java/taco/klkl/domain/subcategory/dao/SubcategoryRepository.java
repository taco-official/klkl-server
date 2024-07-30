package taco.klkl.domain.subcategory.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import taco.klkl.domain.subcategory.domain.Subcategory;

@Repository
public interface SubcategoryRepository extends JpaRepository<Subcategory, Long> {
	List<Subcategory> findAllByCategoryId(Long id);
}
