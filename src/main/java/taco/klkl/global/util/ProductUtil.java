package taco.klkl.global.util;

import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import taco.klkl.domain.category.dto.response.tag.TagResponse;
import taco.klkl.domain.product.dao.ProductRepository;
import taco.klkl.domain.product.domain.Product;
import taco.klkl.domain.product.domain.ProductTag;
import taco.klkl.domain.product.exception.ProductNotFoundException;

@Component
@RequiredArgsConstructor
public class ProductUtil {

	private final ProductRepository productRepository;

	public Product findProductEntityById(final Long id) {
		return productRepository.findById(id)
			.orElseThrow(ProductNotFoundException::new);
	}

	public Page<Product> findProductsByMemberId(final Long memberId, final Pageable pageable) {
		return productRepository.findByMemberId(memberId, pageable);
	}

	public Page<Product> findProductsByMemberIds(final Set<Long> memberIds, final Pageable pageable) {
		return productRepository.findByMemberIds(memberIds, pageable);
	}

	public void validateProductId(final Long id) {
		final boolean existsById = productRepository.existsById(id);
		if (!existsById) {
			throw new ProductNotFoundException();
		}
	}

	public static Set<TagResponse> generateTagsByProduct(final Product product) {
		return Optional.ofNullable(product.getProductTags())
			.map(productTag -> productTag.stream()
				.map(ProductTag::getTag)
				.filter(Objects::nonNull)
				.map(TagResponse::from)
				.collect(Collectors.toSet()))
			.orElse(Collections.emptySet());
	}
}
