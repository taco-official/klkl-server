package taco.klkl.domain.like.service;

import taco.klkl.domain.like.dto.response.LikeResponse;
import taco.klkl.domain.member.domain.Member;
import taco.klkl.domain.product.domain.Product;

public interface LikeService {

	LikeResponse getLike(final Long productId);

	/**
	 * 상품에 좋아요를 누르는 경우
	 * @param productId
	 */
	LikeResponse createLike(final Long productId);

	/**
	 * 상품에 좋아요를 취소하는 경우
	 * @param productId
	 */
	LikeResponse deleteLike(final Long productId);

	/**
	 * 상품에 이미 사용자가 좋아요를 했는지 확인
	 * @param product
	 * @param member
	 * @return
	 */
	boolean isLikePresent(final Product product, final Member member);
}
