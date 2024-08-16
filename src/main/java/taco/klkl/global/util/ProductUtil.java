package taco.klkl.global.util;

import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import taco.klkl.domain.category.dto.response.TagResponse;
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

	public void validateProductId(final Long id) {
		boolean existsById = productRepository.existsById(id);
		if (!existsById) {
			throw new ProductNotFoundException();
		}
	}

	public static Set<TagResponse> createTagsByProduct(final Product product) {
		return Optional.ofNullable(product.getProductTags())
			.map(productTag -> productTag.stream()
				.map(ProductTag::getTag)
				.filter(Objects::nonNull)
				.map(TagResponse::from)
				.collect(Collectors.toSet()))
			.orElse(Collections.emptySet());
	}
}
