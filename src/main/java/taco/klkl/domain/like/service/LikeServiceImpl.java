package taco.klkl.domain.like.service;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import taco.klkl.domain.like.dao.LikeRepository;
import taco.klkl.domain.like.domain.Like;
import taco.klkl.domain.like.dto.response.LikeResponse;
import taco.klkl.domain.product.domain.Product;
import taco.klkl.domain.product.service.ProductService;
import taco.klkl.domain.user.domain.User;
import taco.klkl.global.util.ProductUtil;
import taco.klkl.global.util.UserUtil;

@Slf4j
@Primary
@Service
@Transactional(isolation = Isolation.READ_COMMITTED)
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

	private final LikeRepository likeRepository;

	private final ProductService productService;

	private final UserUtil userUtil;
	private final ProductUtil productUtil;

	@Override
	public LikeResponse createLike(final Long productId) {
		Product product = findProductById(productId);
		User user = findCurrentUser();

		if (isLikePresent(product, user)) {
			return LikeResponse.of(true, product.getLikeCount());
		}

		Like like = Like.of(product, user);
		likeRepository.save(like);
		int likeCount = productService.increaseLikeCount(product);

		return LikeResponse.of(true, likeCount);
	}

	@Override
	public LikeResponse deleteLike(final Long productId) {
		Product product = findProductById(productId);
		User user = findCurrentUser();

		if (isLikePresent(product, user)) {
			likeRepository.deleteByProductAndUser(product, user);
			int likeCount = productService.decreaseLikeCount(product);
			return LikeResponse.of(false, likeCount);
		}

		return LikeResponse.of(false, product.getLikeCount());
	}

	private Product findProductById(final Long productId) {
		return productUtil.findProductEntityById(productId);
	}

	private User findCurrentUser() {
		return userUtil.findCurrentUser();
	}

	@Override
	public boolean isLikePresent(Product product, User user) {
		return likeRepository.existsByProductAndUser(product, user);
	}
}
