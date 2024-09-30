package taco.klkl.domain.product.service;

import java.util.Set;

import org.springframework.data.domain.Pageable;

import taco.klkl.domain.product.domain.Product;
import taco.klkl.domain.product.dto.request.ProductCreateUpdateRequest;
import taco.klkl.domain.product.dto.request.ProductFilterOptions;
import taco.klkl.domain.product.dto.request.ProductSortOptions;
import taco.klkl.domain.product.dto.response.ProductDetailResponse;
import taco.klkl.domain.product.dto.response.ProductSimpleResponse;
import taco.klkl.domain.product.exception.ProductNotFoundException;
import taco.klkl.global.common.response.PagedResponse;

public interface ProductService {

	PagedResponse<ProductSimpleResponse> findProductsByFilterOptionsAndSortOptions(
		final Pageable pageable,
		final ProductFilterOptions filterOptions,
		final ProductSortOptions sortOptions
	);

	PagedResponse<ProductSimpleResponse> findProductsByPartialName(
		final String partialName,
		final Pageable pageable,
		final ProductSortOptions sortOptions
	);

	PagedResponse<ProductSimpleResponse> findFollowingProducts(final Pageable pageable, final Set<Long> userIds);

	ProductDetailResponse findProductById(final Long id) throws ProductNotFoundException;

	ProductDetailResponse createProduct(final ProductCreateUpdateRequest createRequest);

	int increaseLikeCount(Product product);

	int decreaseLikeCount(Product product);

	ProductDetailResponse updateProduct(final Long id, final ProductCreateUpdateRequest updateRequest);

	void deleteProduct(final Long id) throws ProductNotFoundException;

}
