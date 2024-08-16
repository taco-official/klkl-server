package taco.klkl.domain.product.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import taco.klkl.domain.product.domain.ProductTag;

@Repository
public interface ProductTagRepository extends JpaRepository<ProductTag, Long> {
	List<ProductTag> findByProduct_Id(final Long productId);
}

