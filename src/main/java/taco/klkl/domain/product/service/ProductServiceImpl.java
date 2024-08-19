package taco.klkl.domain.product.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Primary;
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
import lombok.extern.slf4j.Slf4j;
import taco.klkl.domain.category.domain.QCategory;
import taco.klkl.domain.category.domain.QSubcategory;
import taco.klkl.domain.category.domain.QTag;
import taco.klkl.domain.category.domain.Subcategory;
import taco.klkl.domain.category.domain.Tag;
import taco.klkl.domain.category.exception.SubcategoryNotFoundException;
import taco.klkl.domain.product.dao.ProductRepository;
import taco.klkl.domain.product.domain.Product;
import taco.klkl.domain.product.domain.QProduct;
import taco.klkl.domain.product.domain.QProductTag;
import taco.klkl.domain.product.domain.Rating;
import taco.klkl.domain.product.domain.SortCriteria;
import taco.klkl.domain.product.dto.request.ProductCreateUpdateRequest;
import taco.klkl.domain.product.dto.request.ProductFilterOptions;
import taco.klkl.domain.product.dto.request.ProductSortOptions;
import taco.klkl.domain.product.dto.response.ProductDetailResponse;
import taco.klkl.domain.product.dto.response.ProductSimpleResponse;
import taco.klkl.domain.product.exception.InvalidCityIdsException;
import taco.klkl.domain.product.exception.ProductNotFoundException;
import taco.klkl.domain.product.exception.SortDirectionNotFoundException;
import taco.klkl.domain.region.domain.City;
import taco.klkl.domain.region.domain.Currency;
import taco.klkl.domain.region.domain.QCity;
import taco.klkl.domain.region.domain.QCountry;
import taco.klkl.domain.region.exception.CityNotFoundException;
import taco.klkl.domain.region.exception.CurrencyNotFoundException;
import taco.klkl.domain.user.domain.User;
import taco.klkl.global.common.response.PagedResponseDto;
import taco.klkl.global.util.CityUtil;
import taco.klkl.global.util.CurrencyUtil;
import taco.klkl.global.util.SubcategoryUtil;
import taco.klkl.global.util.TagUtil;
import taco.klkl.global.util.UserUtil;

@Slf4j
@Primary
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

	private final JPAQueryFactory queryFactory;
	private final ProductRepository productRepository;

	private final UserUtil userUtil;
	private final TagUtil tagUtil;
	private final SubcategoryUtil subcategoryUtil;
	private final CityUtil cityUtil;
	private final CurrencyUtil currencyUtil;

	@Override
	public PagedResponseDto<ProductSimpleResponse> findProductsByFilterOptionsAndSortOptions(
		final Pageable pageable,
		final ProductFilterOptions filterOptions,
		final ProductSortOptions sortOptions
	) {
		validateFilterOptions(filterOptions);

		final JPAQuery<?> baseQuery = createBaseQuery(filterOptions);
		final long total = getCount(baseQuery);
		final List<Product> products = fetchProducts(baseQuery, pageable, sortOptions);
		final Page<Product> productPage = new PageImpl<>(products, pageable, total);

		return PagedResponseDto.of(productPage, ProductSimpleResponse::from);
	}

	@Override
	public ProductDetailResponse findProductById(final Long id) throws ProductNotFoundException {
		final Product product = productRepository.findById(id)
			.orElseThrow(ProductNotFoundException::new);
		return taco.klkl.domain.product.dto.response.ProductDetailResponse.from(product);
	}

	@Override
	@Transactional
	public ProductDetailResponse createProduct(final ProductCreateUpdateRequest createRequest) {
		final Product product = createProductEntity(createRequest);
		productRepository.save(product);
		if (createRequest.tagIds() != null) {
			Set<Tag> tags = createTagsByTagIds(createRequest.tagIds());
			product.addTags(tags);
		}
		return taco.klkl.domain.product.dto.response.ProductDetailResponse.from(product);
	}

	@Override
	@Transactional
	public int increaseLikeCount(Product product) {
		return product.increaseLikeCount();
	}

	@Override
	@Transactional
	public int decreaseLikeCount(Product product) {
		return product.decreaseLikeCount();
	}

	@Override
	@Transactional
	public ProductDetailResponse updateProduct(final Long id, final ProductCreateUpdateRequest updateRequest)
		throws ProductNotFoundException {
		final Product product = productRepository.findById(id)
			.orElseThrow(ProductNotFoundException::new);
		updateProductEntity(product, updateRequest);
		if (updateRequest.tagIds() != null) {
			Set<Tag> updatedTags = createTagsByTagIds(updateRequest.tagIds());
			product.updateTags(updatedTags);
		}
		return ProductDetailResponse.from(product);
	}

	@Override
	@Transactional
	public void deleteProduct(final Long id) throws ProductNotFoundException {
		final Product product = productRepository.findById(id)
			.orElseThrow(ProductNotFoundException::new);
		productRepository.delete(product);
	}

	@Override
	public List<ProductSimpleResponse> getProductsByPartialName(final String partialName) {
		final QProduct product = QProduct.product;
		final QCity city = QCity.city;
		final QCountry country = QCountry.country;
		final QSubcategory subcategory = QSubcategory.subcategory;
		final QCategory category = QCategory.category;

		final List<Product> products = queryFactory
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
		final QProduct product = QProduct.product;
		final QProductTag productTag = QProductTag.productTag;
		final QTag tag = QTag.tag;

		JPAQuery<?> query = queryFactory.from(product);

		final BooleanBuilder builder = new BooleanBuilder();
		builder.and(createCityFilter(filterOptions.cityIds()));
		builder.and(createSubcategoryFilter(filterOptions.subcategoryIds()));
		builder.and(createTagFilter(filterOptions.tagIds()));

		if (filterOptions.tagIds() != null && !filterOptions.tagIds().isEmpty()) {
			query = query.leftJoin(product.productTags, productTag)
				.leftJoin(productTag.tag, tag);
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
		final JPAQuery<Product> productQuery = baseQuery.select(QProduct.product).distinct();

		applySorting(productQuery, sortOptions);

		return productQuery
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();
	}

	private void applySorting(final JPAQuery<Product> query, final ProductSortOptions sortOptions) {
		final PathBuilder<Product> pathBuilder = new PathBuilder<>(Product.class, "product");
		final Sort.Direction sortDirection = createSortDirectionByQuery(sortOptions.sortDirection());
		final SortCriteria sortBy = SortCriteria.fromQuery(sortOptions.sortBy());
		final OrderSpecifier<?> orderSpecifier = new OrderSpecifier<>(
			sortDirection == Sort.Direction.ASC ? Order.ASC : Order.DESC,
			pathBuilder.get(sortBy.getValue(), Comparable.class)
		);
		query.orderBy(orderSpecifier);
	}

	private BooleanExpression createCityFilter(final Set<Long> cityIds) {
		if (cityIds == null || cityIds.isEmpty()) {
			return null;
		}
		return QProduct.product.city.id.in(cityIds);
	}

	private BooleanExpression createSubcategoryFilter(final Set<Long> subcategoryIds) {
		if (subcategoryIds == null || subcategoryIds.isEmpty()) {
			return null;
		}
		return QProduct.product.subcategory.id.in(subcategoryIds);
	}

	private BooleanExpression createTagFilter(final Set<Long> filterIds) {
		if (filterIds == null || filterIds.isEmpty()) {
			return null;
		}
		return QProductTag.productTag.tag.id.in(filterIds);
	}

	private Set<Tag> createTagsByTagIds(final Set<Long> filterIds) {
		return filterIds.stream()
			.map(tagUtil::findTagEntityById)
			.collect(Collectors.toSet());
	}

	private Product createProductEntity(final ProductCreateUpdateRequest createRequest) {
		final Rating rating = Rating.from(createRequest.rating());
		final User user = userUtil.findTestUser();
		final City city = findCityById(createRequest.cityId());
		final Subcategory subcategory = findSubcategoryById(createRequest.subcategoryId());
		final Currency currency = findCurrencyById(createRequest.currencyId());

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
		final City city = findCityById(updateRequest.cityId());
		final Subcategory subcategory = findSubcategoryById(updateRequest.subcategoryId());
		final Currency currency = findCurrencyById(updateRequest.currencyId());

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

	private Sort.Direction createSortDirectionByQuery(final String query) throws SortDirectionNotFoundException {
		try {
			return Sort.Direction.fromString(query);
		} catch (IllegalArgumentException e) {
			throw new SortDirectionNotFoundException();
		}
	}

	private City findCityById(final Long cityId) throws CityNotFoundException {
		return cityUtil.findCityEntityById(cityId);
	}

	private Subcategory findSubcategoryById(final Long subcategoryId) throws SubcategoryNotFoundException {
		return subcategoryUtil.findSubcategoryEntityById(subcategoryId);
	}

	private Currency findCurrencyById(final Long currencyId) throws CurrencyNotFoundException {
		return currencyUtil.findCurrencyEntityById(currencyId);
	}

	private void validateFilterOptions(final ProductFilterOptions filterOptions) {
		if (filterOptions.cityIds() != null) {
			validateCityIds(filterOptions.cityIds());
		}
		if (filterOptions.subcategoryIds() != null) {
			validateSubcategoryIds(filterOptions.subcategoryIds());
		}
		if (filterOptions.tagIds() != null) {
			validateTagIds(filterOptions.tagIds());
		}
	}

	private void validateCityIds(final Set<Long> cityIds) throws InvalidCityIdsException {
		boolean isValidCityIds = cityUtil.isCitiesMappedToSameCountry(cityIds);
		if (!isValidCityIds) {
			throw new InvalidCityIdsException();
		}
	}

	private void validateSubcategoryIds(final Set<Long> subcategoryIds) {
		subcategoryIds.forEach(subcategoryUtil::findSubcategoryEntityById);
	}

	private void validateTagIds(final Set<Long> tagIds) {
		tagIds.forEach(tagUtil::findTagEntityById);
	}
}
