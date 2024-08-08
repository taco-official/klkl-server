package taco.klkl.domain.like.service;

import org.springframework.stereotype.Service;

@Service
public interface LikeService {

	/**
	 * 상품에 좋아요를 누르는 경우
	 * @param productId
	 */
	void createLike(Long productId);

	/**
	 * 상품에 좋아요를 취소하는 경우
	 * @param productId
	 */
	void deleteLike(Long productId);

}
