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
import taco.klkl.domain.member.domain.Member;
import taco.klkl.domain.product.domain.Product;
import taco.klkl.domain.product.service.ProductService;
import taco.klkl.global.util.MemberUtil;
import taco.klkl.global.util.ProductUtil;

@Slf4j
@Primary
@Service
@Transactional(isolation = Isolation.READ_COMMITTED)
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

	private final LikeRepository likeRepository;

	private final ProductService productService;

	private final MemberUtil memberUtil;
	private final ProductUtil productUtil;

	@Override
	@Transactional(readOnly = true)
	public LikeResponse getLike(final Long productId) {
		final Product product = findProductById(productId);
		final Member member = memberUtil.getCurrentMember();
		if (likeRepository.existsByProductAndMember(product, member)) {
			return LikeResponse.of(true, product.getLikeCount());
		}
		return LikeResponse.of(false, product.getLikeCount());
	}

	@Override
	public LikeResponse createLike(final Long productId) {
		final Product product = findProductById(productId);
		final Member member = memberUtil.getCurrentMember();
		if (isLikePresent(product, member)) {
			return LikeResponse.of(true, product.getLikeCount());
		}
		Like like = Like.of(product, member);
		likeRepository.save(like);
		int likeCount = productService.increaseLikeCount(product);
		return LikeResponse.of(true, likeCount);
	}

	@Override
	public LikeResponse deleteLike(final Long productId) {
		final Product product = findProductById(productId);
		final Member member = memberUtil.getCurrentMember();
		if (isLikePresent(product, member)) {
			likeRepository.deleteByProductAndMember(product, member);
			int likeCount = productService.decreaseLikeCount(product);
			return LikeResponse.of(false, likeCount);
		}
		return LikeResponse.of(false, product.getLikeCount());
	}

	private Product findProductById(final Long productId) {
		return productUtil.findProductEntityById(productId);
	}

	@Override
	public boolean isLikePresent(Product product, Member member) {
		return likeRepository.existsByProductAndMember(product, member);
	}
}
