package taco.klkl.global.util;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import taco.klkl.domain.like.dao.LikeRepository;
import taco.klkl.domain.member.domain.Member;
import taco.klkl.domain.product.domain.Product;

@Component
@RequiredArgsConstructor
public class LikeUtil {

	private final LikeRepository likeRepository;

	public boolean isLikedByProductAndMember(final Product product, final Member member) {
		return likeRepository.existsByProductAndMember(product, member);
	}
}
