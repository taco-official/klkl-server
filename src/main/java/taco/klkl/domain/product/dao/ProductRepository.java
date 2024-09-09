package taco.klkl.domain.product.dao;

import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import taco.klkl.domain.product.domain.Product;
import taco.klkl.domain.user.domain.User;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
	Page<Product> findByUserId(final Long userId, final Pageable pageable);

	@Query("SELECT p FROM product p WHERE p.user IN " +
		"(SELECT f.following FROM follow f WHERE f.follower = :follower " +
		"AND (:followingIds IS NULL OR f.following.id IN :followingIds))")
	Page<Product> findProductsOfFollowedUsers(
		@Param("follower") final User follower,
		@Param("followingIds") final Set<Long> followingIds,
		final Pageable pageable
	);
}
