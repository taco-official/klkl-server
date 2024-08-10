package taco.klkl.domain.category.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import taco.klkl.domain.category.domain.Category;
import taco.klkl.domain.category.domain.CategoryName;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
	List<Category> findAllByNameIn(List<CategoryName> names);
}
