package taco.klkl.domain.product.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import taco.klkl.domain.category.dto.response.FilterResponseDto;
import taco.klkl.domain.category.service.FilterService;
import taco.klkl.domain.product.dao.ProductFilterRepository;
import taco.klkl.domain.product.domain.Product;
import taco.klkl.domain.product.domain.ProductFilter;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductFilterService {

	private final ProductFilterRepository productFilterRepository;

	private final ProductService productService;
	private final FilterService filterService;

	public List<FilterResponseDto> getProductFilters(
		final Long productId
	) {
		final List<ProductFilter> productFilters = productFilterRepository.findByProduct_Id(productId);

		return productFilters.stream()
			.map(ProductFilter::getFilter)
			.map(FilterResponseDto::from)
			.toList();
	}

	@Transactional
	public List<FilterResponseDto> createProductFilter(
		final Long productId,
		final List<Long> filterIds
	) {
		final Product product = productService.getProductEntityById(productId);

		final List<ProductFilter> productFilters = filterIds.stream()
			.map(filterService::getFilterEntityById)
			.map(filter -> ProductFilter.of(product, filter))
			.toList();

		productFilterRepository.saveAll(productFilters);

		return productFilters.stream()
			.map(ProductFilter::getFilter)
			.map(FilterResponseDto::from)
			.toList();
	}

	@Transactional
	public List<FilterResponseDto> updateProductFilter(
		final Long productId,
		final List<Long> filterIds
	) {
		final List<ProductFilter> existingProductFilters = productFilterRepository.findByProduct_Id(productId);
		productFilterRepository.deleteAll(existingProductFilters);

		return createProductFilter(productId, filterIds);
	}
}
