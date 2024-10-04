package taco.klkl.domain.product.dao;

import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import taco.klkl.domain.product.domain.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
	Page<Product> findByMemberId(final Long memberId, final Pageable pageable);

	@Query("SELECT p FROM product p WHERE p.member.id IN :memberIds")
	Page<Product> findByMemberIds(
		@Param("memberIds") Set<Long> memberIds,
		Pageable pageable
	);
}
