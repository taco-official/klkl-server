package taco.klkl.domain.like.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import taco.klkl.domain.like.domain.Like;
import taco.klkl.domain.product.domain.Product;
import taco.klkl.domain.user.domain.User;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
	Page<Like> findByUserId(final Long userId, final Pageable pageable);

	void deleteByProductAndUser(final Product product, final User user);

	boolean existsByProductAndUser(final Product product, final User user);
}
