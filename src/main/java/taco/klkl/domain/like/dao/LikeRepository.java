package taco.klkl.domain.like.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import taco.klkl.domain.like.domain.Like;
import taco.klkl.domain.product.domain.Product;
import taco.klkl.domain.user.domain.User;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
	List<Like> findAllByUserId(final Long userId);

	void deleteByProductAndUser(final Product product, final User user);

	boolean existsByProductAndUser(final Product product, final User user);
}
