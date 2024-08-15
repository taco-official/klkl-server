package taco.klkl.domain.product.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import taco.klkl.domain.category.domain.Filter;
import taco.klkl.domain.category.domain.QFilter;
import taco.klkl.domain.category.domain.Subcategory;
import taco.klkl.domain.category.exception.SubcategoryNotFoundException;
import taco.klkl.domain.category.service.FilterService;
import taco.klkl.domain.category.service.SubcategoryService;
import taco.klkl.domain.product.dao.ProductRepository;
import taco.klkl.domain.product.domain.Product;
import taco.klkl.domain.product.domain.QProduct;
import taco.klkl.domain.product.domain.QProductFilter;
import taco.klkl.domain.product.dto.request.ProductCreateUpdateRequestDto;
import taco.klkl.domain.product.dto.request.ProductFilterOptionsDto;
import taco.klkl.domain.product.dto.response.ProductDetailResponseDto;
import taco.klkl.domain.product.dto.response.ProductSimpleResponseDto;
import taco.klkl.domain.product.exception.InvalidCityIdsException;
import taco.klkl.domain.product.exception.ProductNotFoundException;
import taco.klkl.domain.region.domain.City;
import taco.klkl.domain.region.domain.Currency;
import taco.klkl.domain.region.exception.CityNotFoundException;
import taco.klkl.domain.region.exception.CurrencyNotFoundException;
import taco.klkl.domain.region.service.CityService;
import taco.klkl.domain.region.service.CurrencyService;
import taco.klkl.domain.user.domain.User;
import taco.klkl.global.common.response.PagedResponseDto;
import taco.klkl.global.util.UserUtil;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductService {

	private final JPAQueryFactory queryFactory;
	private final ProductRepository productRepository;

	private final CityService cityService;
	private final CurrencyService currencyService;
	private final SubcategoryService subcategoryService;
	private final FilterService filterService;

	private final UserUtil userUtil;

	public PagedResponseDto<ProductSimpleResponseDto> getProductsByFilterOptions(
		Pageable pageable,
		ProductFilterOptionsDto filterOptions
	) {
		validateFilterOptions(filterOptions);

		QProduct product = QProduct.product;
		QProductFilter productFilter = QProductFilter.productFilter;
		QFilter filter = QFilter.filter;

		BooleanBuilder builder = buildFilterOptions(filterOptions, product);

		JPAQuery<Product> query = queryFactory
			.selectDistinct(product)
			.from(product)
			.leftJoin(product.productFilters, productFilter)
			.leftJoin(productFilter.filter, filter)
			.where(builder);

		List<Product> products = query
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		long total = queryFactory
			.select(product.count())
			.from(product)
			.where(builder)
			.fetchOne();

		Page<Product> productPage = new PageImpl<>(products, pageable, total);
		return PagedResponseDto.of(productPage, ProductSimpleResponseDto::from);
	}

	public ProductDetailResponseDto getProductById(final Long id) throws ProductNotFoundException {
		final Product product = productRepository.findById(id)
			.orElseThrow(ProductNotFoundException::new);
		return ProductDetailResponseDto.from(product);
	}

	@Transactional
	public ProductDetailResponseDto createProduct(final ProductCreateUpdateRequestDto createRequest) {
		final Product product = createProductEntity(createRequest);
		productRepository.save(product);
		if (createRequest.filterIds() != null) {
			Set<Filter> filters = getFiltersByFilterIds(createRequest.filterIds());
			product.addFilters(filters);
		}
		return ProductDetailResponseDto.from(product);
	}

	@Transactional
	public ProductDetailResponseDto updateProduct(final Long id, final ProductCreateUpdateRequestDto updateRequest)
		throws ProductNotFoundException {
		final Product product = productRepository.findById(id)
			.orElseThrow(ProductNotFoundException::new);
		updateProductEntity(product, updateRequest);
		if (updateRequest.filterIds() != null) {
			Set<Filter> updatedFilters = getFiltersByFilterIds(updateRequest.filterIds());
			product.updateFilters(updatedFilters);
		}
		return ProductDetailResponseDto.from(product);
	}

	@Transactional
	public void deleteProduct(final Long id) throws ProductNotFoundException {
		final Product product = productRepository.findById(id)
			.orElseThrow(ProductNotFoundException::new);
		productRepository.delete(product);
	}

	private BooleanBuilder buildFilterOptions(
		final ProductFilterOptionsDto options,
		final QProduct product
	) {
		BooleanBuilder builder = new BooleanBuilder();

		if (options.cityIds() != null && !options.cityIds().isEmpty()) {
			builder.and(product.city.cityId.in(options.cityIds()));
		}
		if (options.subcategoryIds() != null && !options.subcategoryIds().isEmpty()) {
			builder.and(product.subcategory.id.in(options.subcategoryIds()));
		}
		if (options.filterIds() != null && !options.filterIds().isEmpty()) {
			builder.and(product.productFilters.any().filter.id.in(options.filterIds()));
		}

		return builder;
	}

	private Set<Filter> getFiltersByFilterIds(final Set<Long> filterIds) {
		return filterIds.stream()
			.map(filterService::getFilterEntityById)
			.collect(Collectors.toSet());
	}

	private Product createProductEntity(final ProductCreateUpdateRequestDto createRequest) {
		final User user = userUtil.findTestUser();
		final City city = getCityEntity(createRequest.cityId());
		final Subcategory subcategory = getSubcategoryEntity(createRequest.subcategoryId());
		final Currency currency = getCurrencyEntity(createRequest.currencyId());

		return Product.of(
			createRequest.name(),
			createRequest.description(),
			createRequest.address(),
			createRequest.price(),
			user,
			city,
			subcategory,
			currency
		);
	}

	private void updateProductEntity(final Product product, final ProductCreateUpdateRequestDto updateRequest) {
		final City city = getCityEntity(updateRequest.cityId());
		final Subcategory subcategory = getSubcategoryEntity(updateRequest.subcategoryId());
		final Currency currency = getCurrencyEntity(updateRequest.currencyId());

		product.update(
			updateRequest.name(),
			updateRequest.description(),
			updateRequest.address(),
			updateRequest.price(),
			city,
			subcategory,
			currency
		);
	}

	private City getCityEntity(final Long cityId) throws CityNotFoundException {
		return cityService.getCityEntityById(cityId);
	}

	private Subcategory getSubcategoryEntity(final Long subcategoryId) throws SubcategoryNotFoundException {
		return subcategoryService.getSubcategoryEntityById(subcategoryId);
	}

	private Currency getCurrencyEntity(final Long currencyId) throws CurrencyNotFoundException {
		return currencyService.getCurrencyEntityById(currencyId);
	}

	private void validateFilterOptions(final ProductFilterOptionsDto filterOptions) {
		if (filterOptions.cityIds() != null) {
			validateCityIds(filterOptions.cityIds());
		}
		if (filterOptions.subcategoryIds() != null) {
			validateSubcategoryIds(filterOptions.subcategoryIds());
		}
		if (filterOptions.filterIds() != null) {
			validateFilterIds(filterOptions.filterIds());
		}
	}

	private void validateCityIds(final Set<Long> cityIds) throws InvalidCityIdsException {
		boolean isValidCityIds = cityService.isCitiesMappedToSameCountry(cityIds);
		if (!isValidCityIds) {
			throw new InvalidCityIdsException();
		}
	}

	private void validateSubcategoryIds(final Set<Long> subcategoryIds) {
		subcategoryIds.forEach(subcategoryService::getSubcategoryEntityById);
	}

	private void validateFilterIds(final Set<Long> filterIds) {
		filterIds.forEach(filterService::getFilterEntityById);
	}
}
