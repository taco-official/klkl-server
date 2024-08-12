package taco.klkl.domain.product.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import taco.klkl.domain.category.domain.Subcategory;
import taco.klkl.domain.category.exception.SubcategoryNotFoundException;
import taco.klkl.domain.category.service.SubcategoryService;
import taco.klkl.domain.product.dao.ProductRepository;
import taco.klkl.domain.product.domain.Product;
import taco.klkl.domain.product.domain.QProduct;
import taco.klkl.domain.product.dto.request.ProductCreateUpdateRequestDto;
import taco.klkl.domain.product.dto.request.ProductFilterOptionsDto;
import taco.klkl.domain.product.dto.response.ProductDetailResponseDto;
import taco.klkl.domain.product.dto.response.ProductSimpleResponseDto;
import taco.klkl.domain.product.exception.InvalidCityIdsException;
import taco.klkl.domain.product.exception.ProductNotFoundException;
import taco.klkl.domain.region.domain.City;
import taco.klkl.domain.region.domain.Currency;
import taco.klkl.domain.region.exception.CityNotFoundException;
import taco.klkl.domain.region.exception.CountryNotFoundException;
import taco.klkl.domain.region.exception.CurrencyNotFoundException;
import taco.klkl.domain.region.service.CityService;
import taco.klkl.domain.region.service.CountryService;
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
	private final CountryService countryService;
	private final CurrencyService currencyService;
	private final SubcategoryService subcategoryService;

	private final UserUtil userUtil;

	public PagedResponseDto<ProductSimpleResponseDto> getProductsByFilterOptions(
		Pageable pageable,
		ProductFilterOptionsDto filterOptions
	) {
		validateFilterOptions(filterOptions);

		QProduct product = QProduct.product;
		BooleanBuilder builder = buildFilterOptions(filterOptions, product);

		List<Product> products = queryFactory.selectFrom(product)
			.where(builder)
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		long total = queryFactory.select(product.count())
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
		return ProductDetailResponseDto.from(product);
	}

	@Transactional
	public ProductDetailResponseDto updateProduct(final Long id, final ProductCreateUpdateRequestDto updateRequest)
		throws ProductNotFoundException {
		final Product product = productRepository.findById(id)
			.orElseThrow(ProductNotFoundException::new);
		updateProductEntity(product, updateRequest);
		return ProductDetailResponseDto.from(product);
	}

	@Transactional
	public void deleteProduct(final Long id) throws ProductNotFoundException {
		final Product product = productRepository.findById(id)
			.orElseThrow(ProductNotFoundException::new);
		productRepository.delete(product);
	}

	public Product getProductEntityById(final Long id) {
		return productRepository.findById(id)
			.orElseThrow(ProductNotFoundException::new);
	}

	public boolean existsProductById(final Long id) {
		return productRepository.existsById(id);
	}

	private BooleanBuilder buildFilterOptions(ProductFilterOptionsDto options, QProduct product) {
		BooleanBuilder builder = new BooleanBuilder();

		if (options.countryId() != null) {
			builder.and(product.city.country.countryId.eq(options.countryId()));
		}
		if (options.cityIds() != null && !options.cityIds().isEmpty()) {
			builder.and(product.city.cityId.in(options.cityIds()));
		}

		return builder;
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
		if (filterOptions.countryId() != null) {
			validateCountryId(filterOptions.countryId());
		}
		if (filterOptions.cityIds() != null) {
			validateCityIds(filterOptions.countryId(), filterOptions.cityIds());
		}
	}

	private void validateCountryId(final Long countryId) throws CountryNotFoundException {
		boolean isValidCountryId = countryService.existsCountryById(countryId);
		if (!isValidCountryId) {
			throw new CountryNotFoundException();
		}
	}

	private void validateCityIds(
		final Long countryId,
		final List<Long> cityIds
	) throws CityNotFoundException {
		boolean isValidCityIds = cityService.isCitiesMappedToSameCountry(countryId, cityIds);
		if (!isValidCityIds) {
			throw new InvalidCityIdsException();
		}
	}
}
