package taco.klkl.domain.product.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import taco.klkl.domain.category.domain.Filter;
import taco.klkl.domain.category.domain.QCategory;
import taco.klkl.domain.category.domain.QFilter;
import taco.klkl.domain.category.domain.QSubcategory;
import taco.klkl.domain.category.domain.Subcategory;
import taco.klkl.domain.category.exception.SubcategoryNotFoundException;
import taco.klkl.domain.category.service.SubcategoryService;
import taco.klkl.domain.product.dao.ProductRepository;
import taco.klkl.domain.product.domain.Product;
import taco.klkl.domain.product.domain.QProduct;
import taco.klkl.domain.product.domain.QProductFilter;
import taco.klkl.domain.product.domain.Rating;
import taco.klkl.domain.product.dto.request.ProductCreateUpdateRequest;
import taco.klkl.domain.product.dto.request.ProductFilterOptions;
import taco.klkl.domain.product.dto.request.ProductSortOptions;
import taco.klkl.domain.product.dto.response.ProductDetailResponse;
import taco.klkl.domain.product.dto.response.ProductSimpleResponse;
import taco.klkl.domain.product.exception.InvalidCityIdsException;
import taco.klkl.domain.product.exception.InvalidSortOptionException;
import taco.klkl.domain.product.exception.ProductNotFoundException;
import taco.klkl.domain.region.domain.City;
import taco.klkl.domain.region.domain.Currency;
import taco.klkl.domain.region.domain.QCity;
import taco.klkl.domain.region.domain.QCountry;
import taco.klkl.domain.region.exception.CityNotFoundException;
import taco.klkl.domain.region.exception.CurrencyNotFoundException;
import taco.klkl.domain.region.service.CityService;
import taco.klkl.domain.region.service.CurrencyService;
import taco.klkl.domain.user.domain.User;
import taco.klkl.global.common.constants.ProductConstants;
import taco.klkl.global.common.response.PagedResponseDto;
import taco.klkl.global.util.FilterUtil;
import taco.klkl.global.util.SubcategoryUtil;
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

	private final UserUtil userUtil;
	private final FilterUtil filterUtil;
	private final SubcategoryUtil subcategoryUtil;

	public PagedResponseDto<ProductSimpleResponse> getProductsByFilterOptions(
		final Pageable pageable,
		final ProductFilterOptions filterOptions,
		final ProductSortOptions sortOptions
	) {
		validateFilterOptions(filterOptions);
		validateSortOptions(sortOptions);

		JPAQuery<?> baseQuery = createBaseQuery(filterOptions);
		long total = getCount(baseQuery);
		List<Product> products = fetchProducts(baseQuery, pageable, sortOptions);

		Page<Product> productPage = new PageImpl<>(products, pageable, total);
		return PagedResponseDto.of(productPage, ProductSimpleResponse::from);
	}

	public ProductDetailResponse getProductById(final Long id) throws ProductNotFoundException {
		final Product product = productRepository.findById(id)
			.orElseThrow(ProductNotFoundException::new);
		return taco.klkl.domain.product.dto.response.ProductDetailResponse.from(product);
	}

	@Transactional
	public ProductDetailResponse createProduct(final ProductCreateUpdateRequest createRequest) {
		final Product product = createProductEntity(createRequest);
		productRepository.save(product);
		if (createRequest.filterIds() != null) {
			Set<Filter> filters = getFiltersByFilterIds(createRequest.filterIds());
			product.addFilters(filters);
		}
		return taco.klkl.domain.product.dto.response.ProductDetailResponse.from(product);
	}

	@Transactional
	public int increaseLikeCount(Product product) {
		return product.increaseLikeCount();
	}

	@Transactional
	public int decreaseLikeCount(Product product) {
		return product.decreaseLikeCount();
	}

	@Transactional
	public ProductDetailResponse updateProduct(final Long id, final ProductCreateUpdateRequest updateRequest)
		throws ProductNotFoundException {
		final Product product = productRepository.findById(id)
			.orElseThrow(ProductNotFoundException::new);
		updateProductEntity(product, updateRequest);
		if (updateRequest.filterIds() != null) {
			Set<Filter> updatedFilters = getFiltersByFilterIds(updateRequest.filterIds());
			product.updateFilters(updatedFilters);
		}
		return ProductDetailResponse.from(product);
	}

	@Transactional
	public void deleteProduct(final Long id) throws ProductNotFoundException {
		final Product product = productRepository.findById(id)
			.orElseThrow(ProductNotFoundException::new);
		productRepository.delete(product);
	}

	public List<ProductSimpleResponse> getProductsByPartialName(String partialName) {

		QProduct product = QProduct.product;
		QCity city = QCity.city;
		QCountry country = QCountry.country;
		QSubcategory subcategory = QSubcategory.subcategory;
		QCategory category = QCategory.category;

		List<Product> products = queryFactory
			.selectFrom(product)
			.join(product.city, city).fetchJoin()
			.join(city.country, country).fetchJoin()
			.join(product.subcategory, subcategory).fetchJoin()
			.join(subcategory.category, category).fetchJoin()
			.where(product.name.contains(partialName))
			.fetch();

		return products.stream()
			.map(ProductSimpleResponse::from)
			.toList();
	}

	private JPAQuery<?> createBaseQuery(final ProductFilterOptions filterOptions) {
		QProduct product = QProduct.product;
		QProductFilter productFilter = QProductFilter.productFilter;
		QFilter filter = QFilter.filter;

		JPAQuery<?> query = queryFactory.from(product);

		BooleanBuilder builder = new BooleanBuilder();
		builder.and(createCityFilter(filterOptions.cityIds()));
		builder.and(createSubcategoryFilter(filterOptions.subcategoryIds()));
		builder.and(createFilterIdsFilter(filterOptions.filterIds()));

		if (filterOptions.filterIds() != null && !filterOptions.filterIds().isEmpty()) {
			query = query.leftJoin(product.productFilters, productFilter)
				.leftJoin(productFilter.filter, filter);
		}

		return query.where(builder);
	}

	private long getCount(JPAQuery<?> baseQuery) {
		return Optional.ofNullable(baseQuery.select(QProduct.product.countDistinct()).fetchOne())
			.orElse(0L);
	}

	private List<Product> fetchProducts(
		final JPAQuery<?> baseQuery,
		final Pageable pageable,
		final ProductSortOptions sortOptions
	) {
		JPAQuery<Product> productQuery = baseQuery.select(QProduct.product).distinct();

		applySorting(productQuery, sortOptions);

		return productQuery
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();
	}

	private void applySorting(final JPAQuery<Product> query, final ProductSortOptions sortOptions) {
		PathBuilder<Product> pathBuilder = new PathBuilder<>(Product.class, "product");
		Sort.Direction sortDirection = Sort.Direction.fromString(sortOptions.sortDirection());
		OrderSpecifier<?> orderSpecifier = new OrderSpecifier<>(
			sortDirection == Sort.Direction.ASC ? Order.ASC : Order.DESC,
			pathBuilder.get(sortOptions.sortBy(), Comparable.class)
		);
		query.orderBy(orderSpecifier);
	}

	private BooleanExpression createCityFilter(final Set<Long> cityIds) {
		if (cityIds == null || cityIds.isEmpty()) {
			return null;
		}
		return QProduct.product.city.cityId.in(cityIds);
	}

	private BooleanExpression createSubcategoryFilter(final Set<Long> subcategoryIds) {
		if (subcategoryIds == null || subcategoryIds.isEmpty()) {
			return null;
		}
		return QProduct.product.subcategory.id.in(subcategoryIds);
	}

	private BooleanExpression createFilterIdsFilter(final Set<Long> filterIds) {
		if (filterIds == null || filterIds.isEmpty()) {
			return null;
		}
		return QProductFilter.productFilter.filter.id.in(filterIds);
	}

	private Set<Filter> getFiltersByFilterIds(final Set<Long> filterIds) {
		return filterIds.stream()
			.map(filterUtil::getFilterEntityById)
			.collect(Collectors.toSet());
	}

	private Product createProductEntity(final ProductCreateUpdateRequest createRequest) {
		final Rating rating = Rating.from(createRequest.rating());
		final User user = userUtil.findTestUser();
		final City city = getCityEntity(createRequest.cityId());
		final Subcategory subcategory = getSubcategoryEntity(createRequest.subcategoryId());
		final Currency currency = getCurrencyEntity(createRequest.currencyId());

		return Product.of(
			createRequest.name(),
			createRequest.description(),
			createRequest.address(),
			createRequest.price(),
			rating,
			user,
			city,
			subcategory,
			currency
		);
	}

	private void updateProductEntity(final Product product, final ProductCreateUpdateRequest updateRequest) {
		final Rating rating = Rating.from(updateRequest.rating());
		final City city = getCityEntity(updateRequest.cityId());
		final Subcategory subcategory = getSubcategoryEntity(updateRequest.subcategoryId());
		final Currency currency = getCurrencyEntity(updateRequest.currencyId());

		product.update(
			updateRequest.name(),
			updateRequest.description(),
			updateRequest.address(),
			updateRequest.price(),
			rating,
			city,
			subcategory,
			currency
		);
	}

	private City getCityEntity(final Long cityId) throws CityNotFoundException {
		return cityService.getCityEntityById(cityId);
	}

	private Subcategory getSubcategoryEntity(final Long subcategoryId) throws SubcategoryNotFoundException {
		return subcategoryUtil.getSubcategoryEntityById(subcategoryId);
	}

	private Currency getCurrencyEntity(final Long currencyId) throws CurrencyNotFoundException {
		return currencyService.getCurrencyEntityById(currencyId);
	}

	private void validateFilterOptions(final ProductFilterOptions filterOptions) {
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

	private void validateSortOptions(final ProductSortOptions sortOptions) throws InvalidSortOptionException {
		if (!ProductConstants.ALLOWED_SORT_BY.contains(sortOptions.sortBy())) {
			throw new InvalidSortOptionException();
		}
		if (!ProductConstants.ALLOWED_SORT_DIRECTION.contains(sortOptions.sortDirection())) {
			throw new InvalidSortOptionException();
		}
	}

	private void validateCityIds(final Set<Long> cityIds) throws InvalidCityIdsException {
		boolean isValidCityIds = cityService.isCitiesMappedToSameCountry(cityIds);
		if (!isValidCityIds) {
			throw new InvalidCityIdsException();
		}
	}

	private void validateSubcategoryIds(final Set<Long> subcategoryIds) {
		subcategoryIds.forEach(subcategoryUtil::getSubcategoryEntityById);
	}

	private void validateFilterIds(final Set<Long> filterIds) {
		filterIds.forEach(filterUtil::getFilterEntityById);
	}
}
