package taco.klkl.global.util;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import taco.klkl.domain.product.dao.ProductRepository;
import taco.klkl.domain.product.domain.Product;
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
}
