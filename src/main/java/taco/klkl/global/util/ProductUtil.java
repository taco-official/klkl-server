package taco.klkl.global.util;

import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import taco.klkl.domain.category.dto.response.FilterResponseDto;
import taco.klkl.domain.product.dao.ProductRepository;
import taco.klkl.domain.product.domain.Product;
import taco.klkl.domain.product.domain.ProductFilter;
import taco.klkl.domain.product.exception.ProductNotFoundException;

@Component
@RequiredArgsConstructor
public class ProductUtil {

	private final ProductRepository productRepository;

	public Product getProductEntityById(final Long id) {
		return productRepository.findById(id)
			.orElseThrow(ProductNotFoundException::new);
	}

	public boolean existsProductById(final Long id) {
		return productRepository.existsById(id);
	}

	public static Set<FilterResponseDto> createFiltersByProduct(final Product product) {
		return Optional.ofNullable(product.getProductFilters())
			.map(productFilters -> productFilters.stream()
				.map(ProductFilter::getFilter)
				.filter(Objects::nonNull)
				.map(FilterResponseDto::from)
				.collect(Collectors.toSet()))
			.orElse(Collections.emptySet());
	}
}
