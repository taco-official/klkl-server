package taco.klkl.domain.like.service;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import taco.klkl.domain.like.dao.LikeRepository;
import taco.klkl.domain.like.domain.Like;
import taco.klkl.domain.product.domain.Product;
import taco.klkl.domain.product.service.ProductService;
import taco.klkl.domain.user.domain.User;
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

	@Override
	public void createLike(Long productId) {
		Product product = getProductById(productId);
		User user = getCurrentUser();

		if (isLikeAlreadyExists(product, user)) {
			return;
		}

		Like like = Like.of(product, user);
		likeRepository.save(like);
		productService.addLikeCount(product);
	}

	@Override
	public void deleteLike(Long productId) {
		Product product = getProductById(productId);
		User user = getCurrentUser();

		if (isLikeAlreadyExists(product, user)) {
			likeRepository.deleteByProductAndUser(product, user);
			productService.subtractLikeCount(product);
		}
	}

	private Product getProductById(Long productId) {
		return productService.getProductById(productId);
	}

	private User getCurrentUser() {
		return userUtil.getCurrentUser();
	}

	private boolean isLikeAlreadyExists(Product product, User user) {
		return likeRepository.existsByProductAndUser(product, user);
	}
}
