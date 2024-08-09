package taco.klkl.domain.like.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import taco.klkl.domain.like.domain.Like;
import taco.klkl.domain.product.domain.Product;
import taco.klkl.domain.user.domain.User;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
	void deleteByProductAndUser(Product product, User user);

	boolean existsByProductAndUser(Product product, User user);
}
