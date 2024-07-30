package taco.klkl.domain.product.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import taco.klkl.domain.product.domain.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
