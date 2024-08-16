package taco.klkl.domain.like.service;

import org.springframework.stereotype.Service;

import taco.klkl.domain.like.dto.response.LikeResponse;
import taco.klkl.domain.product.domain.Product;
import taco.klkl.domain.user.domain.User;

@Service
public interface LikeService {

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
	 * @param user
	 * @return
	 */
	boolean isLikePresent(final Product product, final User user);
}
