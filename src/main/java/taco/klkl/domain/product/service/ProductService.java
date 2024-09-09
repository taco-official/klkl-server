package taco.klkl.domain.product.service;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import taco.klkl.domain.product.domain.Product;
import taco.klkl.domain.product.dto.request.ProductCreateUpdateRequest;
import taco.klkl.domain.product.dto.request.ProductFilterOptions;
import taco.klkl.domain.product.dto.request.ProductSortOptions;
import taco.klkl.domain.product.dto.response.ProductDetailResponse;
import taco.klkl.domain.product.dto.response.ProductSimpleResponse;
import taco.klkl.domain.product.exception.ProductNotFoundException;
import taco.klkl.global.common.response.PagedResponse;

@Service
public interface ProductService {

	PagedResponse<ProductSimpleResponse> findProductsByFilterOptionsAndSortOptions(
		final Pageable pageable,
		final ProductFilterOptions filterOptions,
		final ProductSortOptions sortOptions
	);

	ProductDetailResponse findProductById(final Long id) throws ProductNotFoundException;

	ProductDetailResponse createProduct(final ProductCreateUpdateRequest createRequest);

	int increaseLikeCount(Product product);

	int decreaseLikeCount(Product product);

	ProductDetailResponse updateProduct(final Long id, final ProductCreateUpdateRequest updateRequest);

	void deleteProduct(final Long id) throws ProductNotFoundException;

	PagedResponse<ProductSimpleResponse> findProductsByPartialName(String partialName, Pageable pageable,
		ProductSortOptions sortOptions);

}
