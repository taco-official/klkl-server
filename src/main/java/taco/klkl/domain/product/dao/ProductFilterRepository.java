package taco.klkl.domain.product.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import taco.klkl.domain.product.domain.ProductFilter;

@Repository
public interface ProductFilterRepository extends JpaRepository<ProductFilter, Long> {
	List<ProductFilter> findByProduct_Id(final Long productId);
}

