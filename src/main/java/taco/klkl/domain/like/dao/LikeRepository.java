package taco.klkl.domain.like.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import taco.klkl.domain.like.domain.Like;
import taco.klkl.domain.member.domain.Member;
import taco.klkl.domain.product.domain.Product;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
	Page<Like> findByMemberId(final Long memberId, final Pageable pageable);

	void deleteByProductAndMember(final Product product, final Member member);

	boolean existsByProductAndMember(final Product product, final Member member);
}
