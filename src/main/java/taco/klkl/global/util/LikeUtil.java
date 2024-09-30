package taco.klkl.global.util;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import taco.klkl.domain.like.dao.LikeRepository;
import taco.klkl.domain.like.domain.Like;
import taco.klkl.domain.member.domain.Member;
import taco.klkl.domain.product.domain.Product;

@Component
@RequiredArgsConstructor
public class LikeUtil {

	private final LikeRepository likeRepository;

	public Page<Like> findLikesByMemberId(final Long memberId, final Pageable pageable) {
		return likeRepository.findByMemberId(memberId, pageable);
	}

	public boolean isLikedByProductAndMember(final Product product, final Member member) {
		return likeRepository.existsByProductAndMember(product, member);
	}
}
