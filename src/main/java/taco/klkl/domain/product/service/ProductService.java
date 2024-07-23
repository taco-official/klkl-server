package taco.klkl.domain.product.service;

import java.util.List;

import org.springframework.stereotype.Service;

import taco.klkl.domain.product.domain.Product;
import taco.klkl.domain.product.dto.request.ProductCreateRequestDto;
import taco.klkl.domain.product.dto.request.ProductUpdateRequestDto;

@Service
public interface ProductService {
	Product createProduct(ProductCreateRequestDto productCreateRequestDto);

	Product getProductById(Long id);

	List<Product> getAllProducts();

	Product updateProduct(Long id, ProductUpdateRequestDto productUpdateRequestDto);

	void deleteProduct(Long id);
}
