package taco.klkl.global.util;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import taco.klkl.domain.category.dto.response.tag.TagSimpleResponse;
import taco.klkl.domain.image.dto.response.ImageResponse;
import taco.klkl.domain.member.domain.Member;
import taco.klkl.domain.member.exception.MemberNotFoundException;
import taco.klkl.domain.product.dao.ProductRepository;
import taco.klkl.domain.product.domain.Product;
import taco.klkl.domain.product.domain.ProductImage;
import taco.klkl.domain.product.domain.ProductTag;
import taco.klkl.domain.product.dto.response.ProductDetailResponse;
import taco.klkl.domain.product.dto.response.ProductSimpleResponse;
import taco.klkl.domain.product.exception.ProductNotFoundException;

@Component
@RequiredArgsConstructor
public class ProductUtil {

	private final ProductRepository productRepository;

	private final MemberUtil memberUtil;
	private final LikeUtil likeUtil;

	public Product findProductEntityById(final Long id) {
		return productRepository.findById(id)
			.orElseThrow(ProductNotFoundException::new);
	}

	public Page<Product> findProductsByMemberId(final Long memberId, final Pageable pageable) {
		return productRepository.findByMemberId(memberId, pageable);
	}

	public Page<Product> findProductsByMemberIdIn(final Set<Long> memberIds, final Pageable pageable) {
		return productRepository.findByMemberIdIn(memberIds, pageable);
	}

	public ProductSimpleResponse createProductSimpleResponse(final Product product) {
		try {
			Member currentMember = memberUtil.getCurrentMember();
			final boolean isLiked = likeUtil.isLikedByProductAndMember(product, currentMember);
			return ProductSimpleResponse.from(product, isLiked);
		} catch (MemberNotFoundException e) {
			return ProductSimpleResponse.from(product, false);
		}
	}

	public ProductDetailResponse createProductDetailResponse(final Product product) {
		try {
			Member currentMember = memberUtil.getCurrentMember();
			final boolean isLiked = likeUtil.isLikedByProductAndMember(product, currentMember);
			return ProductDetailResponse.from(product, isLiked);
		} catch (MemberNotFoundException e) {
			return ProductDetailResponse.from(product, false);
		}
	}

	public void validateProductId(final Long id) {
		final boolean existsById = productRepository.existsById(id);
		if (!existsById) {
			throw new ProductNotFoundException();
		}
	}

	public static Set<TagSimpleResponse> generateTagsByProduct(final Product product) {
		return Optional.ofNullable(product.getProductTags())
			.map(productTag -> productTag.stream()
				.map(ProductTag::getTag)
				.filter(Objects::nonNull)
				.map(TagSimpleResponse::from)
				.collect(Collectors.toSet()))
			.orElse(Collections.emptySet());
	}

	public static List<ImageResponse> generateImagesByProduct(final Product product) {
		return product.getImages().stream()
			.map(ProductImage::getImage)
			.map(ImageResponse::from)
			.toList();
	}
}
